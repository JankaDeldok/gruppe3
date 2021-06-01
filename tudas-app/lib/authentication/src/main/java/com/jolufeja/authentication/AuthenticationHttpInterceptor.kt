package com.jolufeja.authentication

import com.jolufeja.httpclient.HttpClientRequest
import com.jolufeja.httpclient.HttpClientResponse
import com.jolufeja.httpclient.HttpInterceptor

class AuthenticationHttpInterceptor(private val authService: UserAuthenticationService) : HttpInterceptor {

    override suspend fun intercept(request: HttpClientRequest, chain: HttpInterceptor.Chain): HttpClientResponse =
        authService.authentication.await().let { authInfo ->
            val newRequest = request.cloneWith {
                headers { headers ->
                    headers.add("Authorization", "Bearer ${authInfo.token}")
                }
            }

            chain.proceed(newRequest)
        }
}