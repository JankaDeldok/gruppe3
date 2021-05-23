package com.jolufeja.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions


@DslMarker
annotation class EventDrivenNavigationDsl

@EventDrivenNavigationDsl
interface EventDrivenNavigationBuilder {


    fun register(
        event: NavigationEvent,
        @IdRes actionId: Int,
        options: NavOptions? = null,
        args: Bundle = Bundle.EMPTY
    )

    fun register(
        event: NavigationEvent,
        @IdRes actionId: Int,
        options: NavOptions? = null,
        argsSpec: Bundle.() -> Unit
    )

    fun <T : NavigationEvent> register(
        eventType: Class<T>,
        @IdRes actionId: Int,
        options: NavOptions? = null,
        argsSpec: Bundle.(T) -> Unit
    )

    fun register(event: NavigationEvent, action: NavController.() -> Unit)

    fun <T : NavigationEvent> register(eventType: Class<T>, action: NavController.(T) -> Unit)

}


inline fun <reified T : NavigationEvent> EventDrivenNavigationBuilder.register(
    @IdRes actionId: Int,
    options: NavOptions? = null,
    noinline argsSpec: Bundle.(T) -> Unit
) = register(T::class.java, actionId, options, argsSpec)

inline fun <reified T : NavigationEvent> EventDrivenNavigationBuilder.regsiter(
    noinline action: NavController.(T) -> Unit
) = register(T::class.java, action)

private class EventDrivenNavigationBuilderImpl(
    private val navController: NavController
) : EventDrivenNavigationBuilder {

    private val navActionsByInstance = mutableMapOf<NavigationEvent, (NavController) -> Unit>()
    private val navActionsByType = mutableMapOf<Class<out NavigationEvent>, (NavController, NavigationEvent) -> Unit>()

    override fun register(event: NavigationEvent, actionId: Int, options: NavOptions?, args: Bundle) {
        navActionsByInstance[event] = { navController ->
            navController.navigate(actionId, args, options)
        }
    }

    override fun register(event: NavigationEvent, actionId: Int, options: NavOptions?, argsSpec: Bundle.() -> Unit) {
        navActionsByInstance[event] = { navController ->
            navController.navigate(
                actionId,
                Bundle().apply(argsSpec),
                options
            )
        }
    }

    override fun <T : NavigationEvent> register(
        eventType: Class<T>,
        actionId: Int,
        options: NavOptions?,
        argsSpec: Bundle.(T) -> Unit
    ) {
        navActionsByType[eventType] = { navController, navigationEvent ->
            val args = Bundle().apply {
                @Suppress("UNCHECKED_CAST")
                argsSpec(navigationEvent as T)
            }
            navController.navigate(actionId, args , options)
        }
    }

    override fun register(event: NavigationEvent, action: NavController.() -> Unit) {
        navActionsByInstance[event] = action
    }

    override fun <T : NavigationEvent> register(eventType: Class<T>, action: NavController.(T) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        navActionsByType[eventType] = action as (NavController, NavigationEvent) -> Unit
    }

    fun build() = NavControllerNavigator(
        navController,
        navActionsByInstance.toMap(),
        navActionsByType.toMap()
    )
}
