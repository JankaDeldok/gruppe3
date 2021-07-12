package com.jolufeja.tudas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jolufeja.tudas.service.challenges.Challenge
import kotlinx.coroutines.channels.Channel


class IndividualChallengeReceivedViewModel(private val challenge: Challenge) : ViewModel() {

    init {
        Log.d("IndividualChallengeReceivedViewModel", challenge.name)
    }

    val name = MutableLiveData(challenge.name)
    val creator = MutableLiveData(challenge.creator)
    val creationDate = MutableLiveData(challenge.creationDate)
    val description = MutableLiveData(challenge.description)
    val dueDate = MutableLiveData(challenge.dueDate)
    val reward = MutableLiveData(challenge.reward)
    val worth = MutableLiveData(challenge.worth)
    val addressedTo = MutableLiveData(challenge.addressedTo)

    val completeChallenge: Channel<Challenge> = Channel()

    fun completeChallenge() {
        Log.d("IndividualChallengeReceivedViewModel", name.value.toString())
        completeChallenge.trySend(challenge)
    }
}