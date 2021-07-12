package com.jolufeja.tudas

import android.util.Log
import androidx.lifecycle.*
import arrow.core.computations.either
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.authentication.UserCredentials
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.navigation.NavigationEvent
import com.jolufeja.navigation.NavigationEventPublisher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LoginNavigationEvents : NavigationEvent {
    PROCEED_TO_REGISTRATION,
    PROCEED_TO_HOME
}

class LoginViewModel(
    private val authenticationService: UserAuthenticationService,
    private val navigator: NavigationEventPublisher
) : ViewModel() {

    val hasLoggedIn: Channel<Unit> = Channel()

    val userNameData: MutableLiveData<String> = MutableLiveData("")
    val passwordData: MutableLiveData<String> = MutableLiveData("")


    private val userNameState: StateFlow<String> =
        userNameData.asFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val passwordState: StateFlow<String> =
        passwordData.asFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")

    val canPerformLogin: LiveData<Boolean> = combine(
        userNameState,
        passwordState
    ) { (userName, password) ->
        userName.isNotEmpty() && password.isNotEmpty()
    }.asLiveData()


    fun performLogin() {
        viewModelScope.launch {
            val credentials = UserCredentials(userNameState.value, passwordState.value)

            authenticationService.login(credentials).fold(
                ifLeft = { Log.d("LoginViewModel", "Login failed. $it") },
                ifRight = {
                    hasLoggedIn.trySend(Unit)
                    navigator.publish(LoginNavigationEvents.PROCEED_TO_HOME)
                }
            )
        }
    }

    fun switchToRegistration() = navigator.publish(LoginNavigationEvents.PROCEED_TO_REGISTRATION)



}