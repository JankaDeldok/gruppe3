package com.jolufeja.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jolufeja.navigation.NavigationEventPublisher
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class BaseViewModel : ViewModel(), KoinComponent {
    protected val navigator: NavigationEventPublisher by inject()
}

inline fun <reified T : BaseViewModel> T.debug(message: String) =
    Log.d(T::class.qualifiedName, message)

inline fun <reified T : BaseViewModel> T.debug(message: String, error: Throwable) =
    Log.d(T::class.qualifiedName, message, error)

inline fun <reified T : BaseViewModel, E> T.debug(message: String, error: E) =
    Log.d(T::class.qualifiedName, "$message\n$error")