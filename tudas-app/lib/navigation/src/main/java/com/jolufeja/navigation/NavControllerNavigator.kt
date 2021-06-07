package com.jolufeja.navigation

import android.util.Log
import androidx.navigation.NavController

internal class NavControllerNavigator(
    private val navController: NavController,
    private val navActionsByInstance: Map<NavigationEvent, (NavController) -> Unit>,
    private val navActionsByType: Map<Class<out NavigationEvent>, (NavController, NavigationEvent) -> Unit>
) : (NavigationEvent) -> Unit {

    override fun invoke(event: NavigationEvent) {
        navActionsByInstance[event]?.invoke(navController)
            ?: navActionsByType[event.javaClass]?.invoke(navController, event)
            ?: run { return }
    }


}