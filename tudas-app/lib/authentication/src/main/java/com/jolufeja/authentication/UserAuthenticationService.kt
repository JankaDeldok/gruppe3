package com.jolufeja.authentication

import arrow.core.Either
import arrow.core.computations.either
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.tryExecute
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration

interface UserAuthenticationService {

    suspend fun login(credentials: UserCredentials): Either<AuthenticationError.LoginFailed, Unit>

    suspend fun logout()

    val authentication: Deferred<AuthenticationInfo>

}


class DefaultUserAuthenticationService(
    private val httpClient: HttpClient,
    private val authStore: AuthenticationStore
) : UserAuthenticationService {

    companion object {
        private object EmptyAuthStoreException : Throwable("Authentication store is empty.")
        private const val LoginUrl = "Set backend login url here"
    }

    private val authCancellable: MutableStateFlow<CacheState> = MutableStateFlow(CacheState.Active)

    override suspend fun login(
        credentials: UserCredentials
    ): Either<AuthenticationError.LoginFailed, Unit> = either {
        val authInfo = httpClient
            .loginCredentialRequest(credentials)
            .bind()

        authStore.store(authInfo)
        authCancellable.value = CacheState.Active
    }

    override suspend fun logout() {
        authStore.clear()
        authCancellable.value = CacheState.Cancelled
    }

    /**
     * This may become relevant if we decide to include expiration dates for JWT auth tokens. In that case,
     * the [cached] delegate can automatically try to refresh or revalidate the token through the backend
     */
    override val authentication: Deferred<AuthenticationInfo> by cached(
        ttlForValue = { Duration.ofDays(100) },
        ttlForError = { Duration.ofMinutes(5) },
        cancellation = authCancellable
    ) { authStore.retrieve() ?: throw EmptyAuthStoreException }


    private suspend fun HttpClient.loginCredentialRequest(
        credentials: UserCredentials
    ): Either<AuthenticationError.LoginFailed, AuthenticationInfo> =
        post(LoginUrl)
            .formBody { form ->
                form.add("username", credentials.username)
                form.add("password", credentials.password)
            }
            .tryExecute()
            .awaitJsonBody<AuthenticationInfo>()
            .mapLeft { AuthenticationError.LoginFailed }

}

