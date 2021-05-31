package com.jolufeja.authentication

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration

interface UserAuthenticationService {

    object EmptyAuthStoreException : Throwable("Authentication store is empty.")


    suspend fun logout()

    val authentication: Deferred<AuthenticationInfo>

}


class DefaultUserAuthenticationService(private val authStore: AuthenticationStore) : UserAuthenticationService {

    private val authCancellable: MutableStateFlow<CacheState> = MutableStateFlow(CacheState.Active)

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