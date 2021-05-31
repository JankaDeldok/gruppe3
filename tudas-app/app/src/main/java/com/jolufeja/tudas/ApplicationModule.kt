package com.jolufeja.tudas

import com.jolufeja.authentication.AuthenticationModule
import com.jolufeja.httpclient.HttpClientModule
import com.jolufeja.navigation.EventDrivenNavigationModule
import org.koin.androidx.fragment.dsl.fragment
import org.koin.core.module.Module
import org.koin.dsl.module

object ApplicationModule {

    private val module = module {
        fragment { ChallengesFragment() }
        fragment { FeedFragment() }
        fragment { HomeFragment() }
        fragment { ProfileFragment() }
        fragment { RankingsFragment() }
    }

    private val dependencies
        get() = sequenceOf(
            HttpClientModule.withDependencies,
            EventDrivenNavigationModule.withDependencies,
            AuthenticationModule.withDependencies
        ).reduce(Set<Module>::plus)

    val withDependencies
        get() = setOf(module) + dependencies
}