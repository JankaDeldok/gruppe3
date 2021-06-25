package com.jolufeja.authentication.registration


data class RegistrationCredentials(
    val name: String,
    val password: String,
    val email: String,
)