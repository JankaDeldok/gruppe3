package com.jolufeja.authentication

import kotlinx.coroutines.Deferred

interface UserAuthenticationService {

    class EmptyAuthStoreException : Throwable("Authentication store is empty.")

    suspend fun loginWithCredentials(credentials: UserCredentials)

    suspend fun logout()

    val authentication: Deferred<AuthenticationInfo>

}