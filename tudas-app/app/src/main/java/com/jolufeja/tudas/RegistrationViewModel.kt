package com.jolufeja.tudas

import android.util.Log
import androidx.lifecycle.*
import com.jolufeja.authentication.registration.RegistrationCredentials
import com.jolufeja.authentication.registration.RegistrationService
import com.jolufeja.navigation.NavigationEvent
import com.jolufeja.navigation.NavigationEventPublisher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


enum class RegistrationNavigationEvents : NavigationEvent {
    PROCEED_TO_HOME,
    PROCEED_TO_LOGIN
}

class RegistrationViewModel(
    private val registrationService: RegistrationService,
    private val navigator: NavigationEventPublisher
) : ViewModel() {

    val userName: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val emailAddress: MutableStateFlow<String> = MutableStateFlow("")

    val canPerformRegistration: StateFlow<Boolean> =
        combine(userName, password, emailAddress) { (name, password, email) ->
            name.isNotBlank() && password.isNotBlank() && email.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun performRegistration() {
        viewModelScope.launch {
            val credentials = RegistrationCredentials(
                userName.value,
                password.value,
                emailAddress.value
            )

            registrationService.registerUser(credentials).fold(
                ifLeft = {
                    Log.d("RegistrationViewModel", "Registration failed due to: $it")
                },
                ifRight = { navigator.publish(LoginNavigationEvents.PROCEED_TO_HOME) }
            )
        }
    }

    fun switchToLogin() = navigator.publish(RegistrationNavigationEvents.PROCEED_TO_LOGIN)
}

