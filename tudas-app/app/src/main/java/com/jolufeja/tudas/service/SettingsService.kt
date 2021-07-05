package com.jolufeja.tudas.service

import arrow.core.Either
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.tryExecute

data class UserSettings(
    val name: String,
    val email: String,
    val password: String
) {

    companion object {
        /*
          Currently missing a way to access the password of the current user.
          Ideally, the password should only be passed if it actually *changed*.
          That way, I don't have to persist the user password anywhere.
        */
        suspend fun UserAuthenticationService.byAuthenticatedUser() {
            val user = authentication.await().user
            return TODO()
        }
    }
}

fun interface SettingsService {
    suspend fun updateSettings(settings: UserSettings): Either<CommonErrors, UserSettings>
}


class DefaultSettingsService(
    private val httpClient: HttpClient
) : SettingsService {

    override suspend fun updateSettings(settings: UserSettings): Either<CommonErrors, UserSettings> =
        httpClient.post("/user/updateSettings")
            .jsonBody(settings)
            .tryExecute()
            .awaitJsonBody()
}