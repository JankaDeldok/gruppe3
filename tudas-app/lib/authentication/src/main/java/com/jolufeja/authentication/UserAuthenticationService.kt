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

    object EmptyAuthStoreException : Throwable("Authentication store is empty.")

    suspend fun login(credentials: UserCredentials): Either<AuthenticationError.LoginFailed, Unit>

    suspend fun logout()

    val authentication: Deferred<AuthenticationInfo>

}


class DefaultUserAuthenticationService(
    private val httpClient: HttpClient,
    private val authStore: AuthenticationStore
) : UserAuthenticationService {

    private val authCancellable: MutableStateFlow<CacheState> = MutableStateFlow(CacheState.Active)

    override suspend fun login(credentials: UserCredentials): Either<AuthenticationError.LoginFailed, Unit> = either {
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

    override val authentication: Deferred<AuthenticationInfo> by cached(
        ttlForValue = { Duration.ofDays(100) },
        ttlForError = { Duration.ofMinutes(5) },
        cancellation = authCancellable
    ) { authStore.retrieve() ?: throw UserAuthenticationService.EmptyAuthStoreException }

}

private suspend fun HttpClient.loginCredentialRequest(
    credentials: UserCredentials
): Either<AuthenticationError.LoginFailed, AuthenticationInfo> = post("TODO()")
    .formBody { form ->
        // We need to decide on the exact structure in which the backend expects login requests to be sent.
        form.add("username", credentials.username)
        form.add("password", credentials.password)
    }
    .tryExecute()
    .awaitJsonBody<AuthenticationInfo>()
    .mapLeft { AuthenticationError.LoginFailed }
