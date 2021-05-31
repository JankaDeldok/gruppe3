package com.jolufeja.authentication

sealed interface AuthenticationError {
    object LoginFailed : AuthenticationError
}
