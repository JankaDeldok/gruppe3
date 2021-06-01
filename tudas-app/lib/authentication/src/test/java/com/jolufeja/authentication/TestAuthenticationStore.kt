package com.jolufeja.authentication


class TestAuthenticationStore : AuthenticationStore {

    companion object {
        const val TokenKey = "authToken"
    }

    private val prefs: MutableMap<String, String> = mutableMapOf()

    override suspend fun retrieve(): AuthenticationInfo? =
        prefs[TokenKey]?.let { AuthenticationInfo(it) }

    override suspend fun store(authInfo: AuthenticationInfo) {
        prefs[TokenKey] = authInfo.token
    }

    override suspend fun clear() {
        prefs.clear()
    }
}