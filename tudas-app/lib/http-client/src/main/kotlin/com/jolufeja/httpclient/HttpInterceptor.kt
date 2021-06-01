package com.jolufeja.httpclient

fun interface HttpInterceptor {

    suspend fun intercept(request: HttpClientRequest, chain: Chain): HttpClientResponse

    fun interface Chain {
        suspend fun proceed(request: HttpClientRequest): HttpClientResponse
    }
}

