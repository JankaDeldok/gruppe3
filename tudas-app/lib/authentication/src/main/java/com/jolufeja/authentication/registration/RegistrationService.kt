package com.jolufeja.authentication.registration

import arrow.core.Either
import arrow.core.computations.either
import com.jolufeja.authentication.AuthenticationInfo
import com.jolufeja.authentication.AuthenticationStore
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.tryExecute


fun interface RegistrationService {
    suspend fun registerUser(
        credentials: RegistrationCredentials
    ): Either<RegistrationError.RegistrationFailed, Unit>
}

class DefaultRegistrationService(
    private val httpClient: HttpClient,
    private val authStore: AuthenticationStore
) : RegistrationService {

    companion object {
        private const val RegistrationUrl: String = "auth/register"
    }

    override suspend fun registerUser(
        credentials: RegistrationCredentials
    ): Either<RegistrationError.RegistrationFailed, Unit> = either {
        val authInfo = httpClient
            .registrationRequest(credentials)
            .bind()

        authStore.store(authInfo)
    }


    private suspend fun HttpClient.registrationRequest(
        credentials: RegistrationCredentials
    ): Either<RegistrationError.RegistrationFailed, AuthenticationInfo> =
        post(RegistrationUrl)
            .jsonBody(credentials)
            .tryExecute()
            .awaitJsonBody<AuthenticationInfo>()
            .mapLeft { RegistrationError.RegistrationFailed(it.message) }


}
