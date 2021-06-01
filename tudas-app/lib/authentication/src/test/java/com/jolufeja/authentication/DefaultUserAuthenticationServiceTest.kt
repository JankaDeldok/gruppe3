package com.jolufeja.authentication

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject


class DefaultUserAuthenticationServiceTest : FunSpec(), KoinTest {

    private val authService: UserAuthenticationService by inject()
    private val authStore: AuthenticationStore by inject()

    init {

            startKoin { modules(AuthenticationTestModule.withDependencies.toList()) }

            test("login") { }

            test("logout clears the AuthenticationStore and succeeds on empty") {
                runBlocking {
                    shouldNotThrowAny { authStore.clear() }
                    shouldNotThrowAny { authStore.retrieve().shouldBeNull() }
                }

            }

            test("authentication") { }
    }
}
