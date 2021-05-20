package com.jolufeja.httpclient

import okhttp3.HttpUrl
import okhttp3.Request

fun httpRequest(spec: Request.Builder.() -> Unit): Request =
    Request.Builder().apply(spec).build()


fun httpGet(spec: Request.Builder.() -> Unit): Request =
    httpRequest { get(); spec() }


fun Request.cloneWith(spec: Request.Builder.() -> Unit): Request =
    newBuilder().apply(spec).build()


fun Request.Builder.url(spec: HttpUrl.Builder.() -> Unit) {
    val url = HttpUrl.Builder().apply(spec).build()
    url(url)
}