package com.jolufeja.tudas.service.challenges

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import arrow.core.Either
import arrow.core.flatMap
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.*
import com.jolufeja.httpclient.error.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


sealed interface ProofKind {
    interface Body

    fun buildJsonBody(challenge: Challenge, userName: String): Body

    data class ProofImage(val image: File) : ProofKind {
        data class WithImageBody(
            val challengeName: String,
            val userName: String
        ) : Body


        override fun buildJsonBody(challenge: Challenge, userName: String) =
            WithImageBody(challenge.name, userName)

        fun contentToRequestBody(challenge: Challenge, userName: String) = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file",image.name, image.asRequestBody("image/jpeg".toMediaType()))
            .addFormDataPart("challengeName", challenge.name)
            .addFormDataPart("userName", userName)
            .build()
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
        return when (proofKind) {
            is ProofKind.ProofImage -> {
                val body = proofKind.contentToRequestBody(challenge, userName)
                httpClient.post("challenge/uploadPicture")
                    .body(body)
                    .tryExecute()
                    .void()
            }
            is ProofKind.SocialMediaLink -> {
                httpClient.post("challenge/uploadsocialmedia")
                    .jsonBody(proofKind.buildJsonBody(challenge, userName))
                    .tryExecute()
                    .void()
            }
        }

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



fun Bitmap.asByteArray(): ByteArray {
    val ostream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG,100,ostream)

    return ostream.toByteArray()
}