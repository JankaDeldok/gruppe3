package com.jolufeja.tudas.service.user

import com.jolufeja.tudas.service.challenges.Challenge

data class User(
    val name: String,
    val password: String,
    val emailAddress: String,
    val profilePicture: String,
    val points: Int,
    val friends: List<FriendEntry>,
    val createdChallenges: List<Challenge>,
    val openChallenges: List<Challenge>,
    val finishedChallenges: List<Challenge>
)