package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun provideUrl(): String = TODO("Provide static tudas backend URL")

object HttpClientModule {

    val module = module {

        single {
            OkHttpClient.Builder().build()
        }

        single(qualifier = named("baseUrl")) {
            { provideUrl().toHttpUrlOrNull()!! }
        }

        factory {
            HttpClient.builder()
                .baseUrl(get(qualifier = named("baseUrl")))
                .okHttpRequestExecutor(get<OkHttpClient>())
                .objectMapper(get())
        }

        single {
            get<HttpClient.Builder>()
                .build()
        }


        single {
            ObjectMapper()
                .findAndRegisterModules()
        }
    }

    val withDependencies
        get() = setOf(module)
}