package com.jolufeja.presentation.viewmodel

import com.jolufeja.httpclient.error.ErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow


interface DataSource<E, V> {

    val status: StateFlow<State<E, V>>

    fun refresh()

    sealed interface State<out E, out V> {
        object Empty : State<Nothing, Nothing>
        object Loading : State<Nothing, Nothing>

        data class Success<out V>(val data: V) : State<Nothing, V>
        data class Error<out E>(val error: E) : State<E, Nothing>
    }

}


private class DefaultDataSource<E, V>(
    override val status: StateFlow<DataSource.State<E, V>>,
    private inline val refresher: () -> Unit
) : DataSource<E, V> {

    override fun refresh() = refresher()

}

@Suppress("FunctionName")
fun <E, V> DataSource(
    fetcher: suspend () -> V,
    errorHandler: ErrorHandler<E>,
    scope: CoroutineScope,
    autoLoad: Boolean = true
): DataSource<E, V> {

    val refreshSignal = Channel<Unit>()

    val status = flow {
        if (!autoLoad) {
            refreshSignal.receive()
        }

        while (true) {
            emit(DataSource.State.Loading)

            val data = try {
                DataSource.State.Success(fetcher())
            } catch (err: Throwable) {
                DataSource.State.Error(errorHandler(err))
            }

            emit(data)
            refreshSignal.receive()
        }
    }.stateIn(scope, SharingStarted.Eagerly, DataSource.State.Empty)

    return DefaultDataSource(status) { refreshSignal.trySend(Unit) }
}
