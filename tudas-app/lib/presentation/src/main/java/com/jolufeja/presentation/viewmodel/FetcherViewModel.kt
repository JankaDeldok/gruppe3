package com.jolufeja.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jolufeja.httpclient.error.ErrorHandler
import kotlinx.coroutines.flow.*

abstract class FetcherViewModel<E, V>(errorHandler: ErrorHandler<E>, autoLoad: Boolean = true) : ViewModel() {

    private val dataSource: DataSource<E, V> =
        DataSource(::fetchData, errorHandler, viewModelScope, autoLoad = autoLoad)

    protected abstract suspend fun fetchData(): V

    val fetchStatus: StateFlow<DataSource.State<E, V>> =
        dataSource.status

    /**
     * Expose the latest value that was produced by the supplied generator.
     * If no value was successfully produced at the point of subscription, this stateflow will return null.
     */
    val data: StateFlow<V?> = fetchStatus
        .filter { it is DataSource.State.Success<V> }
        .map { (it as DataSource.State.Success<V>).data }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun refresh() = dataSource.refresh()

}


