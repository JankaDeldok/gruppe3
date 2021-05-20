package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.InputStream

fun interface HttpRequestExecutor {
    suspend fun execute(request: HttpClientRequest): HttpClientResponse
}

internal class OkHttpRequestExecutor(
    private val callFactory: Call.Factory,
    private val objectMapper: ObjectMapper
) : HttpRequestExecutor {
    override suspend fun execute(request: HttpClientRequest): HttpClientResponse =
        callFactory.newCall(request.toOkHttpRequest())
            .await()
            .let { response ->
                OkHttpClientResponse(response, objectMapper)
            }

    private fun HttpClientRequest.toOkHttpRequest(): Request =
        Request.Builder().run {
            url(url)
            method(method, body)
            headers(headers)
            build()
        }

}

private class OkHttpClientResponse(
    private val response: Response,
    private val objectMapper: ObjectMapper
) : HttpClientResponse {

    override val statusCode: Int
        get() = response.code

    override val statusReason: String
        get() = response.message

    override val headers: Headers
        get() = response.headers

    override val body: ResponseBody?
        get() = response.body


    override suspend fun <T : Any> awaitReadBodyOrNull(reader: (InputStream) -> T): T? =
        body?.let { body ->
            withContext(Dispatchers.IO) {
                body.byteStream().use(reader)
            }
        }


    override suspend fun <T : Any> awaitJsonBodyOrNull(payloadType: JsonTypeSupplier<T>): T? =
        awaitReadBodyOrNull { input ->
            objectMapper.readValue(input, objectMapper.typeFactory.let(payloadType)) as T
        }
}
