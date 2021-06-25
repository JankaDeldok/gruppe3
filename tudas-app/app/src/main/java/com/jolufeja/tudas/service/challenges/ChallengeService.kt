package com.jolufeja.tudas.service.challenges

import arrow.core.Either
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.awaitJsonBodyOrNull
import com.jolufeja.httpclient.error.tryExecute

interface ChallengeService {

    suspend fun createChallenge(challenge: InitialChallenge): Either<CommonErrors, Challenge>

    suspend fun getChallenge(name: String): Either<CommonErrors, Challenge?>
}


class DefaultChallengeService(
    private val httpClient: HttpClient
) : ChallengeService {


    override suspend fun createChallenge(
        challenge: InitialChallenge
    ): Either<CommonErrors, Challenge> =
        httpClient
            .post("/user/addchallenge")
            .jsonBody(challenge)
            .tryExecute()
            .awaitJsonBody()

    override suspend fun getChallenge(
        name: String
    ): Either<CommonErrors, Challenge?> =
        httpClient
            .get("/user/getchallenge")
            .jsonBody("""{ "challengeName": "$name" } """)
            .tryExecute()
            .awaitJsonBodyOrNull()
}