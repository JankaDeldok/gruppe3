package com.jolufeja.authentication

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import java.time.Duration
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.reflect.KProperty

enum class CacheState { Active, Cancelled }

interface CachedValue<T> {

    fun getAsync(): Deferred<T>

    fun getIfPresentAsync(): Deferred<T>?

    suspend fun await(): T = getAsync().await()
}

private class DefaultCachedValue<T>(
    val supplier: suspend () -> T,
    val ttlForValue: (T) -> Duration,
    val ttlForError: (Throwable) -> Duration = { Duration.ZERO },
    state: StateFlow<CacheState>?
) : CachedValue<T> {

    private var currentJob: Job? = null

    @Volatile
    private var deferred: CompletableDeferred<T>? = null
    private val lock: Lock = ReentrantLock()

    init {
        state?.let { stateFlow ->
            CoroutineScope(Dispatchers.Default).launch {
                stateFlow.collect {
                    when (it) {
                        CacheState.Cancelled -> resetCache()
                        else -> {
                        }
                    }
                }
            }
        }
    }


    private fun resetCache() {
        deferred?.cancel()
        currentJob?.cancel()
        deferred = null
        currentJob = null
    }

    override fun getAsync(): Deferred<T> =
        deferred
            ?: lock.withLock {
                deferred
                    ?: run {
                        CompletableDeferred<T>().also { d ->
                            deferred = d
                            currentJob = CoroutineScope(Dispatchers.Default).launch {
                                val ttl = try {
                                    val value = supplier()
                                    d.complete(value)
                                    ttlForValue(value)
                                } catch (err: Throwable) {
                                    d.completeExceptionally(err)
                                    ttlForError(err)
                                }

                                delay(ttl.toMillis())

                                deferred = null
                            }
                        }
                    }
            }

    override fun getIfPresentAsync(): Deferred<T>? = deferred
}

@Suppress("NOTHING_TO_INLINE", "DeferredIsResult")
inline operator fun <T> CachedValue<T>.getValue(thisRef: Any?, property: KProperty<*>): Deferred<T> =
    getAsync()

fun <T> cached(
    ttlForValue: (T) -> Duration = { Duration.ZERO },
    ttlForError: (Throwable) -> Duration = { Duration.ZERO },
    cancellation: StateFlow<CacheState>? = null,
    supplier: suspend () -> T
): CachedValue<T> = DefaultCachedValue(supplier, ttlForValue, ttlForError, cancellation)