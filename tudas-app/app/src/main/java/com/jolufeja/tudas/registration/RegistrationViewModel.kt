package com.jolufeja.tudas.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jolufeja.authentication.registration.RegistrationCredentials
import com.jolufeja.authentication.registration.RegistrationService
import com.jolufeja.navigation.NavigationEventPublisher
import com.jolufeja.tudas.CommonNavigationEvents
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


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
                ifRight = { navigator.publish(CommonNavigationEvents.PROCEED_TO_HOME) }
            )
        }
    }

    fun switchToLogin() = navigator.publish(CommonNavigationEvents.PROCEED_TO_LOGIN)
}

