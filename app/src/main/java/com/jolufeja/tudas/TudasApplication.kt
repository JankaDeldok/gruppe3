package com.jolufeja.tudas

import android.app.Application
import org.koin.core.KoinExperimentalAPI
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

class TudasApplication : Application() {

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TudasApplication)
            fragmentFactory()

            modules(ApplicationModule.withDependencies.toList())
        }


    }
}