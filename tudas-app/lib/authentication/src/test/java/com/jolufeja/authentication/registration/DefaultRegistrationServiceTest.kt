package com.jolufeja.authentication.registration

import arrow.core.Either
import com.jolufeja.authentication.AuthenticationStore
import com.jolufeja.authentication.AuthenticationTestModule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.runBlocking
import org.koin.test.KoinTest
import org.koin.test.inject

fun randomString() = java.util.UUID.randomUUID().toString()

class DefaultRegistrationServiceTest : FunSpec(), KoinTest {

    private val registrationService: RegistrationService by inject()
    private val authStore: AuthenticationStore by inject()

    init {
        AuthenticationTestModule.init()

        test("User registration returns valid auth token for new user") {
            runBlocking {
                val result = registrationService.registerUser(
                    RegistrationCredentials(randomString(), randomString(), randomString())
                )

                when(result) {
                    is Either.Left -> println(result.value.toString())
                }

                result.shouldBeInstanceOf<Either.Right<Unit>>()

                authStore.retrieve().shouldNotBeNull().let(::println)
            }
        }
    }
}
