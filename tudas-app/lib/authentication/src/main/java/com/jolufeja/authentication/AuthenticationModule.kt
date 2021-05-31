package com.jolufeja.authentication

import com.jolufeja.httpclient.HttpClient
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object AuthenticationModule {

    val module = module {

        single<UserAuthenticationService> {
            DefaultUserAuthenticationService(get())
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

    val withDependencies get() = setOf(module)
}