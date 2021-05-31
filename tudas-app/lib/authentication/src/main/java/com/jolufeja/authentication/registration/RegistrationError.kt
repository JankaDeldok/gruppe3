package com.jolufeja.authentication.registration


sealed interface RegistrationError {
    object RegistrationFailed : RegistrationError
}