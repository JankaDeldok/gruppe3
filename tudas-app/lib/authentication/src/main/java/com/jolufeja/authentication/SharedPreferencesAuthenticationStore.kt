package com.jolufeja.authentication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    SharedPreferencesAuthenticationStore.SharedPreferencesName
)


class SharedPreferencesAuthenticationStore(context: Context) : AuthenticationStore {
    companion object {
        const val SharedPreferencesName = "com.jolufeja.authentication"

        val TokenKey = stringPreferencesKey("authToken")
        val TokenTypeKey = stringPreferencesKey("tokenType")
    }

    private val dataStore by lazy { context.dataStore }

    override suspend fun retrieve(): AuthenticationInfo? = dataStore.data
        .map { store ->
            AuthenticationInfo(
                type = store[TokenTypeKey] ?: return@map null,
                token = store[TokenKey] ?: return@map null
            )
        }.first()

    override suspend fun store(authInfo: AuthenticationInfo) {
        dataStore
            .edit {
                it.clear()
                it[TokenKey] = authInfo.token
                it[TokenTypeKey] = authInfo.type
            }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}