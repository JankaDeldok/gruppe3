package com.jolufeja.httpclient.error

import arrow.core.Either
import arrow.core.flatMap
import com.jolufeja.httpclient.HttpClientRequest
import com.jolufeja.httpclient.HttpClientResponse
import com.jolufeja.httpclient.awaitJsonBody
import com.jolufeja.httpclient.awaitJsonBodyOrNull


typealias Result<T> = Either<ApiError, T>

typealias ErrorConstructor = (Unit) -> ApiError

fun interface ErrorHandler<E> : (Throwable) -> E {
    override operator fun invoke(err: Throwable): E
}

interface MapDomain<T, E> : ErrorHandler<E> {
    suspend fun T.toDomain(): Either<E, T>
}


suspend fun HttpClientResponse.readErrorBody(ctor: ErrorConstructor): ApiError =
    awaitJsonBodyOrNull<ErrorBody>()?.let { (placeholder) ->
        ctor(placeholder)
    } ?: ApiError.RequestError(Throwable("Response body is empty - can't construct specific error instance."))


internal val HttpErrorHandler = ErrorHandler<ApiError>(ApiError::RequestError)

suspend fun HttpClientRequest.tryExecute(): Either<ApiError, HttpClientResponse> = with(ApiError.Companion) {
    Either
        .catch { awaitExecute() }
        .mapLeft(this)
        .flatMap { it.toDomain() }
}

suspend fun HttpClientRequest.Builder.tryExecute(): Either<ApiError, HttpClientResponse> =
    build().tryExecute()


inline fun <T> catchError(fn: () -> T): Either<ApiError, T> =
    Either.catch(fn).mapLeft(ApiError)

suspend inline fun <reified T : Any> Either<ApiError, HttpClientResponse>.awaitJsonBody() =
    flatMap { response -> catchError { response.awaitJsonBody<T>() } }


sealed interface ApiError {
    companion object : MapDomain<HttpClientResponse, ApiError> by TODO()
    data class RequestError(val cause: Throwable) : ApiError
}



