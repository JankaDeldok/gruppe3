package com.jolufeja.tudas

import com.jolufeja.authentication.AuthenticationModule
import com.jolufeja.authentication.AuthenticationQualifiers
import com.jolufeja.httpclient.HttpClientModule
import com.jolufeja.navigation.EventDrivenNavigationModule
import com.jolufeja.tudas.service.DefaultSettingsService
import com.jolufeja.tudas.service.SettingsService
import com.jolufeja.tudas.service.challenges.Challenge
import com.jolufeja.tudas.service.challenges.ChallengeService
import com.jolufeja.tudas.service.challenges.DefaultChallengeService
import com.jolufeja.tudas.service.user.DefaultUserService
import com.jolufeja.tudas.service.user.UserService
import get
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object ApplicationModule {

    private val module = module {
        fragment { MainFragment(get(), get()) }
        fragment { ChallengesFragment() }
        fragment { FeedFragment(get()) }
        fragment { ProfileFragment(get(), get()) }
        fragment { RankingsFragment() }
        fragment { LoginFragment(get()) }
        fragment { RegistrationFragment() }
        fragment { IndividualChallengeSentFragment(get()) }
        fragment { ChallengesPublicFragment(get()) }
        fragment { ChallengesReceivedFragment(get()) }
        fragment { ChallengesSentFragment(get()) }
        fragment { RankingsFriendsFragment(get()) }
        fragment { RankingsWorldFragment(get()) }
        fragment { IndividualChallengeReceivedFragment(get()) }
        fragment { IndividualChallengePublicFragment(get()) }
        fragment { AddFriendFragment(get()) }

        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegistrationViewModel(get(), get()) }
        viewModel { IndividualChallengeSentViewModel(get(), get(), get()) }
        viewModel { ChallengesPublicViewModel(get()) }
        viewModel { (challenge: Challenge) ->
            IndividualChallengeReceivedViewModel(challenge)
        }

        viewModel { (challenge: Challenge) ->
            IndividualChallengePublicViewModel(challenge) }

        single<UserService> {
            DefaultUserService(
                get(qualifier(AuthenticationQualifiers.WithUserAuthentication)),
                get()
            )
        }

        single<ChallengeService> {
            DefaultChallengeService(
                get(qualifier(AuthenticationQualifiers.WithUserAuthentication)),
                get()
            )
        }

        single<SettingsService> {
            DefaultSettingsService(get(qualifier(AuthenticationQualifiers.WithUserAuthentication)))
        }
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