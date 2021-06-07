package com.jolufeja.tudas

import androidx.lifecycle.ViewModel
import com.jolufeja.authentication.UserAuthenticationService


class RegistrationViewModel(
    private val authenticationService: UserAuthenticationService
) : ViewModel()