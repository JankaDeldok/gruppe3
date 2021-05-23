package com.jolufeja.httpclient

interface HttpInterceptor {

    suspend fun intercept(request: HttpClientRequest, chain: Chain): HttpClientResponse

    interface Chain {
        suspend fun proceed(request: HttpClientRequest): HttpClientResponse
    }
}

