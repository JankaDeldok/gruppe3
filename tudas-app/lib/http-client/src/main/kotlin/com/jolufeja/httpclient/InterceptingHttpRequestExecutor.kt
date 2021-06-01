package com.jolufeja.httpclient

internal class InterceptingHttpRequestExecutor(
    delegate: HttpRequestExecutor,
    interceptors: List<HttpInterceptor>
) : HttpRequestExecutor {

    private class DelegateChain(
        private val delegate: HttpRequestExecutor
    ) : HttpInterceptor.Chain {
        override suspend fun proceed(request: HttpClientRequest): HttpClientResponse =
            delegate.execute(request)
    }

    private class InterceptingChain(
        private val interceptor: HttpInterceptor,
        private val next: HttpInterceptor.Chain
    ) : HttpInterceptor.Chain {
        override suspend fun proceed(request: HttpClientRequest): HttpClientResponse =
            interceptor.intercept(request, next)
    }

    private val chain: HttpInterceptor.Chain =
        interceptors.fold(DelegateChain(delegate) as HttpInterceptor.Chain) { chain, interceptor ->
            InterceptingChain(interceptor, chain)
        }


    override suspend fun execute(request: HttpClientRequest): HttpClientResponse =
        chain.proceed(request)
}