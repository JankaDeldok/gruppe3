package com.jolufeja.authentication.registration


sealed interface RegistrationError {
   data class RegistrationFailed(val reason: String) : RegistrationError
}