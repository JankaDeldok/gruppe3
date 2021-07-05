package com.jolufeja.tudas.service.user

import arrow.core.Either
import arrow.core.flatMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.catchError
import com.jolufeja.httpclient.error.tryExecute
import com.jolufeja.httpclient.jsonListOf
import com.jolufeja.tudas.service.challenges.UserName
import com.jolufeja.tudas.service.challenges.awaitJsonBody

data class FriendEntry(val friendName: String, val streak: Int)

data class Friendship(val userName: String, val friendName: String)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class PointResult(val points: Int)


interface UserService {

    suspend fun getUser(userName: String): Either<CommonErrors, User?>

    suspend fun addFriend(friendName: String): Either<CommonErrors, User>

    suspend fun removeFriend(friendName: String): Either<CommonErrors, User>

    suspend fun getFriends(userName: String): Either<CommonErrors, List<FriendEntry>>

    suspend fun getPointsOfUser(userName: String): Either<CommonErrors, Int>

    suspend fun getPointsOfCurrentUser(): Either<CommonErrors, Int>

}


class DefaultUserService(
    private val httpClient: HttpClient,
    private val authService: UserAuthenticationService
) : UserService {

    override suspend fun getUser(userName: String): Either<CommonErrors, User?> =
        httpClient.get("user/getuser")
            .jsonBody(UserName(userName))
            .tryExecute()
            .awaitJsonBody()


    override suspend fun addFriend(friendName: String): Either<CommonErrors, User> =
        httpClient.get("user/addfriend")
            .jsonBody(
                Friendship(
                    userName = authService.authentication.await().user.name,
                    friendName = friendName
                )
            )
            .tryExecute()
            .awaitJsonBody()

    override suspend fun removeFriend(friendName: String): Either<CommonErrors, User> =
        httpClient.get("user/removefriend")
            .jsonBody(
                Friendship(
                    userName = authService.authentication.await().user.name,
                    friendName = friendName
                )
            )
            .tryExecute()
            .awaitJsonBody()

    override suspend fun getFriends(userName: String): Either<CommonErrors, List<FriendEntry>> =
        httpClient.get("user/getfriends")
            .jsonBody(UserName(userName))
            .tryExecute()
            .awaitJsonBody(jsonListOf<FriendEntry>())

    override suspend fun getPointsOfUser(userName: String): Either<CommonErrors, Int> =
        httpClient.get("user/getpointsofuser")
            .jsonBody(UserName(userName))
            .tryExecute()
            .awaitJsonBody<PointResult>()
            .map { it.points }

    override suspend fun getPointsOfCurrentUser(): Either<CommonErrors, Int> =
        httpClient.get("user/getpointsofuser")
            .jsonBody(UserName(authService.authentication.await().user.name))
            .tryExecute()
            .awaitJsonBody<PointResult>()
            .map { it.points }

}
