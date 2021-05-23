package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.URI

interface HttpClientRequest {

    val method: String

    val url: HttpUrl

    val headers: Headers

    val body: RequestBody?

    fun cloneAsBuilder(): Builder

    fun cloneWith(spec: Builder.() -> Unit): HttpClientRequest =
        cloneAsBuilder().apply(spec).build()

    suspend fun awaitExecute(): HttpClientResponse


    interface Builder {

        fun url(url: HttpUrl): Builder

        fun url(link: URI) =
            url(link.toString())

        fun url(link: String): Builder

        fun url(link: String?, spec: HttpUrl.Builder.() -> Unit): Builder

        fun url(spec: (HttpUrl.Builder) -> Unit) =
            url(null, spec)

        fun headers(headers: Headers): Builder

        fun headers(spec: (Headers.Builder) -> Unit): Builder

        fun body(body: RequestBody?): Builder

        fun jsonBody(payload: Any, contentType: MediaType = JsonRequestBody.DEFAULT_MEDIA_TYPE): Builder

        fun formBody(spec: (FormBody.Builder) -> Unit): Builder =
            body(FormBody.Builder().apply(spec).build())

        fun build(): HttpClientRequest

        suspend fun awaitExecute(): HttpClientResponse =
            build().awaitExecute()

    }

    companion object {
        fun builder(objectMapper: ObjectMapper, requestExecutor: HttpRequestExecutor, method: String): Builder =
            DefaultHttpClientRequest.Builder(objectMapper, requestExecutor, method)
    }

}

private class DefaultHttpClientRequest(
    override val method: String,
    override val url: HttpUrl,
    override val headers: Headers,
    override val body: RequestBody?,
    private val objectMapper: ObjectMapper,
    private val requestExecutor: HttpRequestExecutor
) : HttpClientRequest {
    override suspend fun awaitExecute(): HttpClientResponse =
        requestExecutor.execute(this)


    override fun cloneAsBuilder(): HttpClientRequest.Builder =
        Builder(objectMapper, requestExecutor, this)


    class Builder private constructor(
        private val objectMapper: ObjectMapper,
        private val requestExecutor: HttpRequestExecutor,
        val method: String,
        var url: HttpUrl?,
        var headers: Headers?,
        var body: RequestBody?
    ) : HttpClientRequest.Builder {

        private companion object {
            val EmptyHeaders = Headers.headersOf()
        }


        internal constructor(
            objectMapper: ObjectMapper, requestExecutor: HttpRequestExecutor, request: HttpClientRequest
        ) : this(objectMapper, requestExecutor, request.method, request.url, request.headers, request.body)


        internal constructor(objectMapper: ObjectMapper, requestExecutor: HttpRequestExecutor, method: String = "GET")
                : this(objectMapper, requestExecutor, method, null, null, null)


        override fun url(url: HttpUrl) = apply {
            this.url = url
        }


        override fun url(link: String) = url(
            this.url?.resolve(link) ?: link.toHttpUrl()
        )


        override fun url(link: String?, spec: HttpUrl.Builder.() -> Unit) = run {
            val urlBuilder = this.url
                ?.let {
                    if (link != null) it.newBuilder(link) else it.newBuilder()
                }
                ?: link?.toHttpUrl()?.newBuilder()
                ?: HttpUrl.Builder()
            url(urlBuilder.apply(spec).build())
        }


        override fun headers(headers: Headers) = apply {
            this.headers = headers
        }


        override fun headers(spec: Headers.Builder.() -> Unit) =
            headers(
                (headers?.newBuilder() ?: Headers.Builder()).apply(spec).build()
            )


        override fun body(body: RequestBody?) = apply {
            this.body = body
        }


        override fun jsonBody(payload: Any, contentType: MediaType) = body(
            JsonRequestBody(payload, objectMapper, contentType)
        )


        override fun build(): HttpClientRequest =
            DefaultHttpClientRequest(
                method = method,
                url = checkNotNull(url) { "Request URL must be set" },
                headers = headers ?: EmptyHeaders,
                body = body,
                objectMapper = objectMapper,
                requestExecutor = requestExecutor
            )
    }
}
