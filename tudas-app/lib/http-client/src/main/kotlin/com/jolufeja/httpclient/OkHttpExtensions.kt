package com.jolufeja.httpclient

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Call.await(): Response =
    suspendCancellableCoroutine { continuation ->

        continuation.invokeOnCancellation { cancel() }

        enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response) {}
            }

            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }
        })
    }


suspend fun Call.Factory.execute(request: Request): Response =
    newCall(request).await()