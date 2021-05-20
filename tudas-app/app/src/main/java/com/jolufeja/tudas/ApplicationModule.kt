package com.jolufeja.tudas

import com.jolufeja.httpclient.HttpClientModule
import org.koin.core.module.Module
import org.koin.dsl.module

object ApplicationModule {

    val module = module {  }

    private val dependencies
        get() = sequenceOf(
            HttpClientModule.withDependencies
        ).reduce(Set<Module>::plus)

    val withDependencies
        get() = setOf(module) + dependencies
}