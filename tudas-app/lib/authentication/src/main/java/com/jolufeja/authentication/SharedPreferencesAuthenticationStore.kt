package com.jolufeja.authentication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import arrow.core.computations.nullable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@JsonIgnoreProperties(ignoreUnknown = true)
data class FeedEntry(val message: String, val new: Boolean)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfo(val id: String, val name: String, val email: String, val feed: List<FeedEntry> = listOf())

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    SharedPreferencesAuthenticationStore.SharedPreferencesName
)

class SharedPreferencesAuthenticationStore(context: Context) : AuthenticationStore {
    companion object {
        const val SharedPreferencesName = "com.jolufeja.authentication"

        val TokenKey = stringPreferencesKey("authToken")
        val UserNameKey = stringPreferencesKey("userName")
        val UserIdKey = stringPreferencesKey("userId")
        val EmailKey = stringPreferencesKey("emailAddress")
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
                        email = store[EmailKey].bind(),
                        feed = listOf()
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
                it[EmailKey] = authInfo.user.email
            }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}