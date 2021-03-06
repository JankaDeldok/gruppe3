package com.jolufeja.tudas.service.user

import arrow.core.Either
import arrow.core.flatMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jolufeja.authentication.FeedEntry
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.HttpClientResponse
import com.jolufeja.httpclient.JsonTypeSupplier
import com.jolufeja.httpclient.error.*
import com.jolufeja.httpclient.jsonListOf
import com.jolufeja.tudas.data.FeedItem
import com.jolufeja.tudas.service.challenges.UserName
import com.jolufeja.tudas.service.challenges.awaitJsonBody

@JsonIgnoreProperties(ignoreUnknown = true)
data class Ranking(val name: String, val points: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FriendEntry(val name: String, val streak: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Friendship(val userName: String, val friendName: String)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class FriendResult(val friends: List<FriendEntry>)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class PointResult(val points: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class FeedResult(val name: String, val feed: List<FeedEntry>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddedFriendResult(val name: String, val friends: List<FriendEntry>)


interface UserService {

    suspend fun getUser(userName: String): Either<CommonErrors, User?>

    suspend fun addFriend(friendName: String): Either<CommonErrors, AddedFriendResult>

    suspend fun removeFriend(friendName: String): Either<CommonErrors, AddedFriendResult>

    suspend fun getFriends(userName: String): Either<CommonErrors, List<FriendEntry>>

    suspend fun getFriendsOfCurrentUser(): Either<CommonErrors, List<FriendEntry>>

    suspend fun getPointsOfUser(userName: String): Either<CommonErrors, Int>

    suspend fun getPointsOfCurrentUser(): Either<CommonErrors, Int>

    suspend fun getFeed(): Either<CommonErrors, List<FeedEntry>>

    suspend fun getFriendsRanking(): Either<CommonErrors, List<Ranking>>

    suspend fun getPublicRanking(): Either<CommonErrors, List<Ranking>>

}


class DefaultUserService(
    private val httpClient: HttpClient,
    private val authService: UserAuthenticationService
) : UserService {

    override suspend fun getUser(userName: String): Either<CommonErrors, User?> =
        httpClient.post("user/getuser")
            .jsonBody(UserName(userName))
            .tryExecute()
            .awaitJsonBody()


    override suspend fun addFriend(friendName: String): Either<CommonErrors, AddedFriendResult> =
        httpClient.post("user/addfriend")
            .jsonBody(
                Friendship(
                    userName = authService.authentication.await().user.name,
                    friendName = friendName
                )
            )
            .tryExecute()
            .awaitJsonBody()

    override suspend fun removeFriend(friendName: String): Either<CommonErrors, AddedFriendResult> =
        httpClient.post("user/removefriend")
            .jsonBody(
                Friendship(
                    userName = authService.authentication.await().user.name,
                    friendName = friendName
                )
            )
            .tryExecute()
            .awaitJsonBody()

    override suspend fun getFriends(userName: String): Either<CommonErrors, List<FriendEntry>> =
        httpClient.post("user/getfriends")
            .jsonBody(UserName(userName))
            .tryExecute()
            .awaitJsonBodyOrNull<FriendResult>()
            .map { it?.friends ?: emptyList() }

    override suspend fun getFriendsOfCurrentUser(): Either<CommonErrors, List<FriendEntry>> =
        httpClient.post("user/getfriends")
            .jsonBody(UserName(authService.authentication.await().user.name))
            .tryExecute()
            .awaitJsonBodyOrNull<FriendResult>()
            .map { it?.friends ?: emptyList() }

    override suspend fun getPointsOfUser(userName: String): Either<CommonErrors, Int> =
        httpClient.post("user/getpointsofuser")
            .jsonBody(UserName(userName))
            .tryExecute()
            .awaitJsonBody<PointResult>()
            .map { it.points }

    override suspend fun getPointsOfCurrentUser(): Either<CommonErrors, Int> =
        httpClient.post("user/getpointsofuser")
            .jsonBody(UserName(authService.authentication.await().user.name))
            .tryExecute()
            .awaitJsonBody<PointResult>()
            .map { it.points }

    override suspend fun getFeed(): Either<CommonErrors, List<FeedEntry>> =
        httpClient.post("user/getfeed")
            .jsonBody(UserName(authService.authentication.await().user.name))
            .tryExecute()
            .awaitJsonBody<FeedResult>()
            .map { it.feed }

    override suspend fun getFriendsRanking(): Either<CommonErrors, List<Ranking>> =
        httpClient.post("user/getfriendranking")
            .jsonBody(UserName(authService.authentication.await().user.name))
            .tryExecute()
            .awaitJsonBody(jsonListOf<Ranking>())

    override suspend fun getPublicRanking(): Either<CommonErrors, List<Ranking>> =
        httpClient.get("user/getpublicranking")
            .tryExecute()
            .awaitJsonBody(jsonListOf<Ranking>())

}

suspend fun <T : Any> Either<CommonErrors, HttpClientResponse>.awaitJsonBodyOrNull(
    payloadType: JsonTypeSupplier<T>
): Either<CommonErrors, T?> =
    flatMap { response -> catchError { response.awaitJsonBodyOrNull(payloadType) } }
