package com.jolufeja.tudas.service.challenges

import android.graphics.Bitmap
import android.util.Log
import arrow.core.Either
import arrow.core.flatMap
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.*
import com.jolufeja.httpclient.error.*
import okhttp3.RequestBody
import okio.Buffer
import java.nio.ByteBuffer


sealed interface ProofKind {
    interface Body

    fun buildJsonBody(challenge: Challenge, userName: String): Body

    data class ProofImage(val image: Bitmap) : ProofKind {
        data class WithImageBody(
            val challengeName: String,
            val userName: String,
            val file: ByteArray
        ) : Body

        override fun buildJsonBody(challenge: Challenge, userName: String) =
            WithImageBody(challenge.name, userName, image.toByteArray())
    }

    data class SocialMediaLink(val link: String) : ProofKind {
        data class WithLinkBody(
            val challengeName: String,
            val userName: String,
            val url: String
        ) : Body

        override fun buildJsonBody(challenge: Challenge, userName: String) =
            WithLinkBody(challenge.name, userName, link)
    }
}

interface ChallengeService {

    suspend fun createChallenge(challenge: InitialChallenge): Either<CommonErrors, Unit>

    suspend fun getChallenge(name: String): Either<CommonErrors, Challenge?>

    suspend fun getPublicChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getFriendChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getOwnCreatedChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getFinishedChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun getOpenChallenges(): Either<CommonErrors, List<Challenge>>

    suspend fun finishChallengeWithProof(
        challenge: Challenge,
        proofKind: ProofKind
    ): Either<CommonErrors, Unit>

}

data class UserName(val userName: String)
data class ChallengeName(val challengeName: String)


class DefaultChallengeService(
    private val httpClient: HttpClient,
    private val authenticationService: UserAuthenticationService
) : ChallengeService {


    override suspend fun createChallenge(
        challenge: InitialChallenge
    ): Either<CommonErrors, Unit> =
        httpClient.post("challenge/addchallenge")
            .jsonBody(challenge)
            .tryExecute()
            .void()

    override suspend fun getChallenge(
        name: String
    ): Either<CommonErrors, Challenge?> =
        httpClient.post("challenge/getchallenge")
            .jsonBody(ChallengeName(name))
            .tryExecute()
            .awaitJsonBodyOrNull()

    override suspend fun getPublicChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.get("challenge/getpublicchallenges")
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getFriendChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.post("challenge/getchallengesfromfriends")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getOwnCreatedChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.post("user/getcreatedchallenges")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getFinishedChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.post("user/getfinishedchallenges")
            .jsonBodyOfCurrentUser()
            .tryExecute()
            .awaitJsonBody(jsonListOf<Challenge>())

    override suspend fun getOpenChallenges(): Either<CommonErrors, List<Challenge>> =
        httpClient.post("user/getopenchallenges")
             .jsonBodyOfCurrentUser()
             .tryExecute()
             .awaitJsonBody(jsonListOf<Challenge>())


    override suspend fun finishChallengeWithProof(
        challenge: Challenge,
        proofKind: ProofKind
    ): Either<CommonErrors, Unit> {
        val userName = authenticationService.authentication.await().user.name
        val body = proofKind.buildJsonBody(challenge, userName)
        val endpoint = when (proofKind) {
            is ProofKind.ProofImage -> httpClient.post("challenge/uploadPicture")
            is ProofKind.SocialMediaLink -> httpClient.post("challenge/uploadsocialmedia")
        }

        return endpoint
            .jsonBody(body)
            .tryExecute()
            .void()
    }


    private suspend fun HttpClientRequest.Builder.jsonBodyOfCurrentUser() =
        jsonBody(UserName(authenticationService.authentication.await().user.name))

    private fun HttpClientRequest.Builder.emptyBody(): HttpClientRequest.Builder =
        jsonBody("")
}


suspend fun <T : Any> Either<CommonErrors, HttpClientResponse>.awaitJsonBody(
    payloadType: JsonTypeSupplier<T>
): Either<CommonErrors, T> =
    flatMap { response -> catchError { response.awaitJsonBody(payloadType) } }



fun Bitmap.toByteArray(): ByteArray = ByteBuffer.allocate(byteCount).apply {
    copyPixelsToBuffer(this)
    rewind()
}.array()