package com.jolufeja.tudas

import androidx.lifecycle.asLiveData
import arrow.core.Either
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.presentation.viewmodel.FetcherViewModel
import com.jolufeja.tudas.service.challenges.Challenge
import com.jolufeja.tudas.service.challenges.ChallengeService
import kotlinx.coroutines.flow.map

class IndividualChallengeReceivedViewModel(
    private val challengeName: String,
    private val challengeService: ChallengeService
) : FetcherViewModel<CommonErrors, Challenge>(ChallengeErrorHandler, autoLoad = true) {

    val name = data.map { it?.name }.asLiveData()
    val creator = data.map { it?.creator }.asLiveData()
    val creationDate = data.map { it?.creationDate }.asLiveData()
    val description = data.map { it?.description }.asLiveData()
    val dueDate = data.map { it?.dueDate }.asLiveData()
    val reward = data.map { it?.reward }.asLiveData()
    val worth = data.map { it?.worth }.asLiveData()
    val addressedTo = data.map { it?.addressedTo }.asLiveData()

    override suspend fun fetchData(): Challenge =
        when (val challenge = challengeService.getChallenge(challengeName)) {
            is Either.Left -> throw ChallengeErrors.FailedToGetChallenge
            is Either.Right -> challenge.value ?: throw ChallengeErrors.NonExistentChallenge
        }
}