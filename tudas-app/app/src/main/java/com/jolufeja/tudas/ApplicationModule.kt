package com.jolufeja.tudas

import com.jolufeja.authentication.AuthenticationModule
import com.jolufeja.httpclient.HttpClientModule
import com.jolufeja.navigation.EventDrivenNavigationModule
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ApplicationModule {

    private val module = module {
        fragment { MainFragment(get()) }
        fragment { ChallengesFragment() }
        fragment { FeedFragment() }
        fragment { HomeFragment() }
        fragment { ProfileFragment() }
        fragment { RankingsFragment() }
        fragment { LoginFragment() }
        fragment { RegistrationFragment() }

        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegistrationViewModel(get(), get()) }
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