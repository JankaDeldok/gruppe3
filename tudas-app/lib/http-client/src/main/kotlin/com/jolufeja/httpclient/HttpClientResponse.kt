package com.jolufeja.httpclient

import okhttp3.Headers
import okhttp3.ResponseBody
import java.io.InputStream

interface HttpClientResponse {

    val statusCode: Int

    val statusReason: String

    val headers: Headers

    val body: ResponseBody?

    suspend fun <T : Any> awaitReadBodyOrNull(reader: (InputStream) -> T): T?

    suspend fun <T : Any> awaitJsonBodyOrNull(payloadType: JsonTypeSupplier<T>): T?

    suspend fun <T : Any> awaitJsonBody(payloadType: JsonTypeSupplier<T>): T =
        awaitJsonBodyOrNull(payloadType) ?: throw MissingResponseBodyException()

}

suspend inline fun <reified T : Any> HttpClientResponse.awaitJsonBodyOrNull(): T? =
    awaitJsonBodyOrNull(jsonTypeOf<T>())

suspend inline fun <reified T : Any> HttpClientResponse.awaitJsonBody(): T =
    awaitJsonBody(jsonTypeOf<T>())