package com.jolufeja.navigation

import org.koin.dsl.bind
import org.koin.dsl.module


object EventDrivenNavigationModule {

    private val module = module {
        single { DefaultNavigationEventBus() }
            .bind<NavigationEventBus>()
            .bind<NavigationEventPublisher>()
    }

    val withDependencies
        get() = setOf(module)
}