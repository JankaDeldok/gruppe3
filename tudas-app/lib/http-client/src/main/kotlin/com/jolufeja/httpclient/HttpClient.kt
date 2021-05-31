package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

internal typealias UrlSupplier = () -> HttpUrl


interface HttpClient : RequestBuilderFactory, HttpRequestExecutor  {

    companion object {
        fun builder(): Builder =
            DefaultHttpClient.Builder()
    }

    interface Builder {

        fun baseUrl(urlProvider: UrlSupplier): Builder

        fun okHttpRequestExecutor(callFactory: Call.Factory): Builder

        fun objectMapper(mapper: ObjectMapper): Builder

        fun addInterceptor(interceptor: HttpInterceptor): Builder

        fun build(): HttpClient
    }
}

private class DefaultHttpClient(
    private val requestBuilderFactory: RequestBuilderFactory,
    private val requestExecutor: HttpRequestExecutor
) : HttpClient, RequestBuilderFactory by requestBuilderFactory, HttpRequestExecutor by requestExecutor {

    class Builder : HttpClient.Builder {
        private var objectMapper: ObjectMapper? = null
        private var baseUrlProvider: UrlSupplier? = null
        private var callFactory: Call.Factory? = null
        private val interceptors = mutableListOf<HttpInterceptor>()


        override fun baseUrl(urlProvider: UrlSupplier) = apply {
            this.baseUrlProvider = urlProvider
        }


        override fun okHttpRequestExecutor(callFactory: Call.Factory) = apply {
            this.callFactory = callFactory
        }


        override fun objectMapper(mapper: ObjectMapper) = apply {
            this.objectMapper = mapper
        }


        override fun addInterceptor(interceptor: HttpInterceptor): HttpClient.Builder = apply {
            interceptors.add(interceptor)
        }


        override fun build(): HttpClient {

            val objectMapper = this.objectMapper ?: jacksonObjectMapper()

            val requestExecutor = OkHttpRequestExecutor(
                callFactory = callFactory ?: OkHttpClient(),
                objectMapper = objectMapper
            ).run {
                if (interceptors.isNotEmpty()) {
                    InterceptingHttpRequestExecutor(this, interceptors)
                } else this
            }

            val requestBuilderFactory = DefaultRequestBuilderFactory(objectMapper, requestExecutor)
                .run {
                    baseUrlProvider?.let { BaseUrlAwareRequestBuilderFactory(this, it) } ?: this
                }

            return DefaultHttpClient(requestBuilderFactory, requestExecutor)
        }
    }
}