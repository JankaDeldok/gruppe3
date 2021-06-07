package com.jolufeja.tudas.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.authentication.UserCredentials
import com.jolufeja.navigation.NavigationEventPublisher
import com.jolufeja.tudas.CommonNavigationEvents
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class LoginViewModel(
    private val authenticationService: UserAuthenticationService,
    private val navigator: NavigationEventPublisher
) : ViewModel() {

    val userName: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")

    val canPerformLogin: StateFlow<Boolean> = combine(userName, password) { (name, password) ->
        name.isNotEmpty() && password.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    fun performLogin() {
        viewModelScope.launch {
            val credentials = UserCredentials(userName.value, password.value)

            authenticationService.login(credentials).fold(
                ifLeft = { Log.d("LoginViewModel", "Login failed due to: $it") },
                ifRight = { navigator.publish(CommonNavigationEvents.PROCEED_TO_HOME) }
            )
        }
    }

    fun switchToRegistration() = navigator.publish(CommonNavigationEvents.PROCEED_TO_REGISTRATION)

}