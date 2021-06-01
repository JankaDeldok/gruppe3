package com.jolufeja.authentication

interface AuthenticationStore {

    suspend fun retrieve(): AuthenticationInfo?

    suspend fun store(authInfo: AuthenticationInfo)

    suspend fun clear()
}
