package com.jolufeja.presentation


import androidx.fragment.app.Fragment
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@InternalCoroutinesApi
class FragmentObserver<T>(
    fragment: Fragment,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {
    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            FlowObserver(viewLifecycleOwner, flow, collector)
        }
    }
}

@InternalCoroutinesApi
inline fun <reified T> Flow<T>.observe(
    fragment: Fragment,
    noinline collector: suspend (T) -> Unit
) = FragmentObserver(fragment, this, collector)

@InternalCoroutinesApi
inline fun <reified T> Flow<T>.observeIn(
    fragment: Fragment
) = FragmentObserver(fragment, this) {}

