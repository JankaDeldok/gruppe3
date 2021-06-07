package com.jolufeja.tudas


import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.authentication.registration.RegistrationError

import com.jolufeja.httpclient.error.ErrorHandler
import com.jolufeja.presentation.viewmodel.FetcherViewModel

private val RegistrationErrorHandler = ErrorHandler { RegistrationError.RegistrationFailed(it) }

class RegistrationViewModel(
    private val authenticationService: UserAuthenticationService
) : FetcherViewModel<RegistrationError, Unit>(autoLoad = false) {

    override suspend fun fetchData() {
        TODO("Not yet implemented")
    }

}