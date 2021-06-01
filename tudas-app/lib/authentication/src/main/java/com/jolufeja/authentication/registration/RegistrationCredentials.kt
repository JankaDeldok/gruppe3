package com.jolufeja.authentication.registration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RegistrationCredentials(
    val name: String,
    val email: String,
    val password: String
)