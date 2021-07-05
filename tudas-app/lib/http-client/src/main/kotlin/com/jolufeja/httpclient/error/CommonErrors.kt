package com.jolufeja.httpclient.error

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.jolufeja.httpclient.HttpClientRequest
import com.jolufeja.httpclient.HttpClientResponse
import com.jolufeja.httpclient.awaitJsonBody
import com.jolufeja.httpclient.awaitJsonBodyOrNull


typealias Result<T> = Either<CommonErrors, T>

typealias ErrorConstructor = (String) -> CommonErrors

fun interface ErrorHandler<E> : (Throwable) -> E {
    override operator fun invoke(err: Throwable): E
}

interface MapDomain<T, E> : ErrorHandler<E> {
    suspend fun T.toDomain(): Either<E, T>
}


suspend fun HttpClientResponse.readErrorBody(ctor: ErrorConstructor): CommonErrors =
    awaitJsonBodyOrNull<ErrorBody>()?.let { err ->
        ctor(err.msg)
    }
        ?: CommonErrors.GenericError("Response body is empty - can't construct specific error instance.")


val HttpErrorHandler = ErrorHandler<CommonErrors>(CommonErrors::GenericError)

internal object HttpMapDomain :
    MapDomain<HttpClientResponse, CommonErrors>,
    ErrorHandler<CommonErrors> by HttpErrorHandler {

    override suspend fun HttpClientResponse.toDomain(): Either<CommonErrors, HttpClientResponse> =
        when (statusCode) {
            in 500..600 -> CommonErrors.InternalServerError.left()
            in 400..410 -> try {
                readErrorBody(CommonErrors::BadRequest)
            } catch (err: Throwable) {
                CommonErrors.GenericError("Request failed with $statusCode, but cant read error body.")
            }.left()
            in 200..305 -> this.right()
            else -> CommonErrors.GenericError("Request successful, but response has unrecognisable status code.")
                .left()
        }
}

suspend fun HttpClientRequest.tryExecute(): Either<CommonErrors, HttpClientResponse> =
    with(CommonErrors.Companion) {
        Either
            .catch { awaitExecute() }
            .mapLeft(this)
            .flatMap { it.toDomain() }
    }

suspend fun HttpClientRequest.Builder.tryExecute(): Either<CommonErrors, HttpClientResponse> =
    build().tryExecute()


inline fun <T> catchError(fn: () -> T): Either<CommonErrors, T> =
    Either.catch(fn).mapLeft(CommonErrors)

suspend inline fun <reified T : Any> Either<CommonErrors, HttpClientResponse>.awaitJsonBody() =
    flatMap { response -> catchError { response.awaitJsonBody<T>() } }


suspend inline fun <reified T : Any> Either<CommonErrors, HttpClientResponse>.awaitJsonBodyOrNull(): Either<CommonErrors, T?> =
    flatMap { response -> catchError { response.awaitJsonBodyOrNull<T>() } }


sealed interface CommonErrors {
    companion object : MapDomain<HttpClientResponse, CommonErrors> by HttpMapDomain

    val message: String

    data class GenericError(override val message: String, val cause: Throwable? = null) :
        CommonErrors {
        constructor(message: String) : this(message, null)
        constructor(error: Throwable) : this(error.message ?: "Unknown error.")
    }

    data class BadRequest(override val message: String) : CommonErrors

    object InternalServerError : CommonErrors {
        override val message = "Internal server error."
    }
}



