package com.jolufeja.httpclient

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URI

interface RequestBuilder {

    fun method(method: String, body: RequestBody? = null): RequestBuilder

    fun get() = method("GET")

    fun head() = method("HEAD")

    fun post(body: RequestBody? = null) = method("POST", body)

    fun put(body: RequestBody? = null) = method("PUT", body)

    fun delete(body: RequestBody? = null) = method("DELETE", body)

    fun url(url: HttpUrl): RequestBuilder

    fun url(link: String): RequestBuilder

    fun url(link: String?, spec: HttpUrl.Builder.() -> Unit): RequestBuilder

    fun url(spec: HttpUrl.Builder.() -> Unit) = url(null, spec)

    fun url(url: URI) =
        url(url.toString())

    fun headers(headers: Headers): RequestBuilder

    fun headers(spec: Headers.Builder.() -> Unit): RequestBuilder

    fun build(): Request
}

@Suppress("FunctionName")
fun RequestBuilder(): RequestBuilder = DefaultRequestBuilder()


private class DefaultRequestBuilder : RequestBuilder {

    private var method: String = "GET"
    private var url: HttpUrl? = null
    private var headers: Headers? = null
    private var body: RequestBody? = null


    override fun method(method: String, body: RequestBody?) = apply {
        this.method = method
        this.body = body
    }


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


    override fun build(): Request =
        Request.Builder().run {
            method(method, body)
            url(checkNotNull(url) { "url is not set" })
            headers?.let { headers(it) }
            build()
        }

}
