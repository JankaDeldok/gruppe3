package com.jolufeja.authentication

/**
 * Authentication qualifiers intended to be used later on to distinguish http-clients that
 * provide JWT-based authentication, or not. We can use koin qualifiers to specifically request a
 * http-client *with* [WithUserAuthentication] or *without* [None] (omitting qualifiers) authentication.
 */
enum class AuthenticationQualifiers {
    None,
    WithUserAuthentication
}