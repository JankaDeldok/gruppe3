package com.jolufeja.authentication

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticationInfo(val token: String)