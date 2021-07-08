package com.jolufeja.tudas.service.challenges

import arrow.core.Either
import arrow.core.flatMap
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.*
import com.jolufeja.httpclient.error.*

interface ChallengeService {

    suspend fun createChallenge(challenge: InitialChallenge): Either<CommonErrors, Challenge>

    suspend fun getChallenge(name: String): Either<CommonErrors, Challenge?>

    suspend fun getPublicChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getFriendChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getOwnCreatedChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getFinishedChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getOpenChallenges(): Either<CommonErrors, List<Challenge>>
}

data class UserName(val userName: String)
data class ChallengeName(val challengeName: String)


class DefaultChallengeService(
    private val httpClient: HttpClient,
    private val authenticationService: UserAuthenticationService
) : ChallengeService {


    override suspend fun createChallenge(
        challenge: InitialChallenge
    ): Either<CommonErrors, Challenge> =
        httpClient.post("challenge/addchallenge")
            .jsonBody(challenge)
            .tryExecute()
            .awaitJsonBody()

    override suspend fun getChallenge(
        name: String
    ): Either<CommonErrors, Challenge?> =
        httpClient.get("challenge/getchallenge")
            .jsonBody(ChallengeName(name))
            .tryExecute()
            .awaitJsonBodyOrNull()

    override suspend fun getPublicChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.get("challenge/getpublicchallenges")
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getFriendChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.get("challenge/getchallengesfromfriends")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getOwnCreatedChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.get("user/getcreatedchallenges")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getFinishedChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.get("user/getfinishedchallenges")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getOpenChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.get("user/getopenchallenges")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())





    private suspend fun HttpClientRequest.Builder.jsonBodyOfCurrentUser() =
        jsonBody(UserName(authenticationService.authentication.await().user.name))

    private fun HttpClientRequest.Builder.emptyBody(): HttpClientRequest.Builder =
        jsonBody("")
}


suspend fun <T : Any> Either<CommonErrors, HttpClientResponse>.awaitJsonBody(
    payloadType: JsonTypeSupplier<T>
): Either<CommonErrors, T> =
    flatMap { response -> catchError { response.awaitJsonBody(payloadType) } }