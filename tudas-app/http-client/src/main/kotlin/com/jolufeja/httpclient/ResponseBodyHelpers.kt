package com.jolufeja.httpclient

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Response
import java.io.InputStream


fun <T> Response.readBody(reader: (InputStream) -> T): T? =
    body?.byteStream()?.use { input ->
        reader(input)
    }


fun <T : Any> Response.jsonBody(objectMapper: ObjectMapper, type: Class<T>): T? =
    readBody { input ->
        objectMapper.readValue(input, type)
    }


inline fun <reified T : Any> Response.jsonBody(objectMapper: ObjectMapper): T? =
    jsonBody(objectMapper, T::class.java)


fun <T : Any> Response.jsonBody(objectMapper: ObjectMapper, type: TypeReference<T>): T? =
    readBody { input ->
        objectMapper.readerFor(type).readValue(input)
    }
