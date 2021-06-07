package com.jolufeja.authentication

import com.jolufeja.authentication.registration.DefaultRegistrationService
import com.jolufeja.authentication.registration.RegistrationService
import com.jolufeja.httpclient.HttpClientModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

object AuthenticationTestModule {


    /**
     * Noop method to force the one-off call to the init block of this object.
     */
    internal fun init() {}

    private val module = module {
        single<RegistrationService> { DefaultRegistrationService(get(), get()) }
        single<AuthenticationStore> { TestAuthenticationStore() }
        single<UserAuthenticationService> { DefaultUserAuthenticationService(get(), get()) }
    }

    private val withDependencies = setOf(module) + HttpClientModule.withDependencies

    init {
        startKoin { modules(withDependencies.toList()) }
    }
}