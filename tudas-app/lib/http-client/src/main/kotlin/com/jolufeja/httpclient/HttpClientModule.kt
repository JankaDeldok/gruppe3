package com.jolufeja.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun provideUrl(): String = "http://10.0.2.2:3000/"

object HttpClientModule {

    private val module = module {

        single {
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build()


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