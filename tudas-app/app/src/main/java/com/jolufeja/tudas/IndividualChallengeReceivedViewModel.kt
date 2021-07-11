package com.jolufeja.tudas

import androidx.lifecycle.ViewModel
import com.jolufeja.tudas.service.challenges.Challenge
import kotlinx.coroutines.channels.Channel


class IndividualChallengeReceivedViewModel(private val challenge: Challenge) : ViewModel() {

    val name = challenge.name
    val creator = challenge.creator
    val creationDate = challenge.creationDate
    val description = challenge.description
    val dueDate = challenge.dueDate
    val reward = challenge.reward
    val worth = challenge.worth
    val addressedTo = challenge.addressedTo

    val completeChallenge: Channel<Challenge> = Channel()

    fun completeChallenge() {
        completeChallenge.trySend(challenge)
    }
}