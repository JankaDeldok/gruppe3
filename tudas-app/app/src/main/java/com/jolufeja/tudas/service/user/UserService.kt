package com.jolufeja.tudas.service.user

import arrow.core.Either
import arrow.core.flatMap
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.HttpClient
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.httpclient.error.awaitJsonBody
import com.jolufeja.httpclient.error.catchError
import com.jolufeja.httpclient.error.tryExecute
import com.jolufeja.httpclient.jsonListOf

data class FriendEntry(val friendName: String, val streak: Int)

data class Friendship(val userName: String, val friendName: String)


interface UserService {

    suspend fun getUser(userName: String): Either<CommonErrors, User?>

    suspend fun addFriend(friendName: String): Either<CommonErrors, User>

    suspend fun removeFriend(friendName: String): Either<CommonErrors, User>

    suspend fun getFriends(userName: String): Either<CommonErrors, List<FriendEntry>>

}


class DefaultUserService(
    private val httpClient: HttpClient,
    private val authService: UserAuthenticationService
) : UserService {

    override suspend fun getUser(userName: String): Either<CommonErrors, User?> =
        httpClient.get("user/getuser")
            .jsonBody("""{ "userName": "$userName" } """)
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
            .jsonBody("""{ "userName": "$userName" } """)
            .tryExecute()
            .flatMap { response -> catchError { response.awaitJsonBody(jsonListOf<FriendEntry>()) } }

}
