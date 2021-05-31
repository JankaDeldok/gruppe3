package com.jolufeja.authentication.registration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RegistrationCredentials(
    val firstName: String,
    val lastName: String,
    val userName: String,
    val emailAddress: String,
    val password: String
)