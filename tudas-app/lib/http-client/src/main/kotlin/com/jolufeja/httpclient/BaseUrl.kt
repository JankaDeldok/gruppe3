package com.jolufeja.httpclient


internal class BaseUrlAwareRequestBuilderFactory(
    private val delegate: RequestBuilderFactory,
    private val baseUrlProvider: UrlSupplier
) : RequestBuilderFactory {

    override fun request(method: String): HttpClientRequest.Builder =
        delegate.request(method)
            .url(baseUrlProvider())
}
