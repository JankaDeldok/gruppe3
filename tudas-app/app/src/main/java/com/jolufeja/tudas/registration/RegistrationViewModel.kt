package com.jolufeja.tudas.registration

import androidx.lifecycle.viewModelScope
import com.jolufeja.authentication.registration.RegistrationCredentials
import com.jolufeja.authentication.registration.RegistrationService
import com.jolufeja.presentation.viewmodel.BaseViewModel
import com.jolufeja.presentation.viewmodel.debug
import com.jolufeja.tudas.CommonNavigationEvents
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class RegistrationViewModel(
    private val registrationService: RegistrationService
) : BaseViewModel() {

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
                ifLeft = { debug("Registration failed", it) },
                ifRight = { navigator.publish(CommonNavigationEvents.PROCEED_TO_HOME) }
            )
        }
    }

    fun switchToLogin() = navigator.publish(CommonNavigationEvents.PROCEED_TO_LOGIN)
}

