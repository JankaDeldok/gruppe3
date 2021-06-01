package com.jolufeja.authentication

sealed interface AuthenticationError {
   data class LoginFailed(val reason: String) : AuthenticationError
}
