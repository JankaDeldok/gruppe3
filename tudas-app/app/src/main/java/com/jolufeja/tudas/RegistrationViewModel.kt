package com.jolufeja.tudas

import android.util.Log
import androidx.lifecycle.*
import com.jolufeja.authentication.registration.RegistrationCredentials
import com.jolufeja.authentication.registration.RegistrationService
import com.jolufeja.navigation.NavigationEvent
import com.jolufeja.navigation.NavigationEventPublisher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


enum class RegistrationNavigationEvents : NavigationEvent {
    PROCEED_TO_HOME,
    PROCEED_TO_LOGIN
}

class RegistrationViewModel(
    private val registrationService: RegistrationService,
    private val navigator: NavigationEventPublisher
) : ViewModel() {

    val userNameData: MutableLiveData<String> = MutableLiveData("")
    val passwordData: MutableLiveData<String> = MutableLiveData("")
    val emailAddressData: MutableLiveData<String> = MutableLiveData("")


    private val userNameState: StateFlow<String> =
        userNameData.asFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val passwordState: StateFlow<String> =
        passwordData.asFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val emailAddressState: StateFlow<String> =
        emailAddressData.asFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")

    val canPerformRegistration: LiveData<Boolean> = combine(
        userNameState,
        passwordState,
        emailAddressState
    ) { (userName, password, emailAddress) ->
        userName.isNotBlank() && password.isNotBlank() && emailAddress.isNotBlank()
    }.asLiveData()

    fun performRegistration() {
        viewModelScope.launch {
            val credentials = RegistrationCredentials(userNameState.value, passwordState.value, emailAddressState.value)

            registrationService.registerUser(credentials).fold(
                ifLeft = { Log.d("RegistrationViewModel", "Registration failed. $it") },
                ifRight = {
                    navigator.publish(LoginNavigationEvents.PROCEED_TO_HOME)
                }
            )
        }
    }

    fun switchToLogin() = navigator.publish(RegistrationNavigationEvents.PROCEED_TO_LOGIN)
}

