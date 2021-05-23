package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink

class JsonRequestBody(
    private val payload: Any,
    private val objectMapper: ObjectMapper,
    private val contentType: MediaType = DEFAULT_MEDIA_TYPE
) : RequestBody() {

    companion object {
        val DEFAULT_MEDIA_TYPE: MediaType = "application/json;charset=UTF-8".toMediaType()
    }

    private val jsonData: Buffer by lazy(LazyThreadSafetyMode.NONE) {
        Buffer().also { buffer ->
            buffer.outputStream().use { output ->
                objectMapper.writeValue(output, payload)
            }
        }
    }

    override fun contentType(): MediaType =
        this.contentType


    override fun contentLength(): Long =
        jsonData.size


    override fun writeTo(sink: BufferedSink) {
        jsonData.use { jsonData ->
            sink.outputStream().use { output ->
                jsonData.writeTo(output)
            }
        }
    }
}