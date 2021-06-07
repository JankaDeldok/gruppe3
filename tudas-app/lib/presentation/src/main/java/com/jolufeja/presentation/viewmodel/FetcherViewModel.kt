package com.jolufeja.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.jolufeja.httpclient.error.ErrorHandler
import kotlinx.coroutines.flow.*

abstract class FetcherViewModel<E, V>(
    errorHandler: ErrorHandler<E>,
    autoLoad: Boolean = true
) : BaseViewModel() {

    private val dataSource: DataSource<E, V> =
        DataSource(::fetchData, errorHandler, viewModelScope, autoLoad = autoLoad)

    protected abstract suspend fun fetchData(): V

    val fetchStatus: StateFlow<DataSource.State<E, V>> =
        dataSource.status

    val data: StateFlow<V?> = fetchStatus
        .filterIsInstance<DataSource.State.Success<V>>()
        .map { it.data }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val hasError: StateFlow<Boolean> = fetchStatus
        .filterIsInstance<DataSource.State.Error<E>>()
        .map { true }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)


    fun refresh() = dataSource.refresh()

}


