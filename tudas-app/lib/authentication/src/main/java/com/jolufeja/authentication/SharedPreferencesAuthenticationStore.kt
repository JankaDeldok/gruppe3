package com.jolufeja.authentication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import arrow.core.computations.nullable
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    SharedPreferencesAuthenticationStore.SharedPreferencesName
)


class SharedPreferencesAuthenticationStore(context: Context) : AuthenticationStore {
    companion object {
        const val SharedPreferencesName = "com.jolufeja.authentication"

        val TokenKey = stringPreferencesKey("authToken")
        val UserNameKey = stringPreferencesKey("userName")
        val UserIdKey = stringPreferencesKey("userId")
    }

    private val dataStore by lazy { context.dataStore }


    override suspend fun retrieve(): AuthenticationInfo? = dataStore.data
        .map { store ->
            nullable {
                AuthenticationInfo(
                    token = store[TokenKey].bind(),
                    user = UserInfo(
                        id = store[UserIdKey].bind(),
                        name = store[UserNameKey].bind(),
                    )
                )
            }

        }.firstOrNull()

    override suspend fun store(authInfo: AuthenticationInfo) {
        dataStore
            .edit {
                it.clear()
                it[TokenKey] = authInfo.token
                it[UserIdKey] = authInfo.user.id
                it[UserNameKey] = authInfo.user.name
            }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}