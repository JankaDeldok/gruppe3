package com.jolufeja.authentication

import com.jolufeja.authentication.registration.DefaultRegistrationService
import com.jolufeja.authentication.registration.RegistrationService
import com.jolufeja.httpclient.HttpClientModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

object AuthenticationTestModule {


    internal fun init() {
        startKoin { modules(withDependencies.toList()) }
    }

    private val module = module {
        single<RegistrationService> { DefaultRegistrationService(get(), get()) }
        single<AuthenticationStore> { TestAuthenticationStore() }
        single<UserAuthenticationService> { DefaultUserAuthenticationService(get(), get()) }
    }

    val withDependencies = setOf(module) + HttpClientModule.withDependencies
}