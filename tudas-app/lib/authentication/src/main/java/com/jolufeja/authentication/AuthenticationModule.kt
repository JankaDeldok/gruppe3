package com.jolufeja.authentication

import com.jolufeja.authentication.registration.RegistrationModule
import com.jolufeja.httpclient.HttpClient
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object AuthenticationModule {

    private val module = module {

        single<UserAuthenticationService> {
            DefaultUserAuthenticationService(get(), get())
        }

        single<AuthenticationStore> {
            SharedPreferencesAuthenticationStore(get())
        }

        single(qualifier(AuthenticationQualifiers.WithUserAuthentication)) {
            get<HttpClient.Builder>()
                .addInterceptor(AuthenticationHttpInterceptor(get()))
                .build()
        }
    }

    val withDependencies get() = setOf(module) + RegistrationModule.withDependencies
}