package com.jolufeja.authentication.registration

import org.koin.dsl.module

object RegistrationModule {

    private val module = module {
         single<RegistrationService> { DefaultRegistrationService(get(), get()) }
    }

    val withDependencies get() = setOf(module)
}