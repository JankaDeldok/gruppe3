package com.jolufeja.navigation

import kotlinx.coroutines.flow.*

interface NavigationEventPublisher {

    fun publish(event: NavigationEvent)
}

interface NavigationEventBus {

    fun asFlow(): Flow<NavigationEvent>

    suspend fun subscribe(subscriber: suspend (NavigationEvent) -> Unit) =
        asFlow().collect(subscriber)
}

internal class DefaultNavigationEventBus : NavigationEventBus, NavigationEventPublisher {

    private val _sharedFlow = MutableSharedFlow<NavigationEvent>(10)

    override fun asFlow(): Flow<NavigationEvent> =
        _sharedFlow.asSharedFlow()

    override fun publish(event: NavigationEvent) {
        _sharedFlow.tryEmit(event)
    }
}