package com.jolufeja.tudas.service

import arrow.core.Either
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.tryExecute

data class UserSettings(
    val name: String,
    val email: String,
    val password: String
)

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