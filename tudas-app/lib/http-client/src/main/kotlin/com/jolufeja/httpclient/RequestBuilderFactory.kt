package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper

interface RequestBuilderFactory {

    fun request(method: String): HttpClientRequest.Builder

    fun get() = request("GET")

    fun get(url: String) = get().url(url)

    fun head() = request("HEAD")

    fun head(url: String) = head().url(url)

    fun post() = request("POST")

    fun post(url: String) = post().url(url)

    fun put() = request("PUT")

    fun put(url: String) = put().url(url)

    fun delete() = request("DELETE")

    fun delete(url: String) = delete().url(url)

}

internal class DefaultRequestBuilderFactory(
    private val objectMapper: ObjectMapper,
    private val requestExecutor: HttpRequestExecutor
) : RequestBuilderFactory {

    override fun request(method: String): HttpClientRequest.Builder =
        HttpClientRequest.builder(objectMapper, requestExecutor, method)
}