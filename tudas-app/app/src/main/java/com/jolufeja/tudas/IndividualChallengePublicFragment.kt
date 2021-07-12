package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentChallengePublicInfoBinding
import com.jolufeja.tudas.service.challenges.Challenge
import com.jolufeja.tudas.service.challenges.ChallengeService
import com.jolufeja.tudas.service.challenges.ProofKind
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class IndividualChallengePublicViewModel(private val challenge: Challenge) : ViewModel() {

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


class IndividualChallengePublicFragment(
    private val challengeService: ChallengeService
) :
    DataBoundFragment<IndividualChallengePublicViewModel, FragmentChallengePublicInfoBinding>(
        R.layout.fragment_challenge_public_info,
        IndividualChallengePublicViewModel::class,
        BR.challengePublicViewModel
    ) {

    override val viewModel: IndividualChallengePublicViewModel by viewModel {
        val challenge =
            arguments?.getSerializable(CHALLENGE_KEY)
                ?: throw MissingChallengeName
        parametersOf(challenge)
    }

    override fun createBinding(view: View) = FragmentChallengePublicInfoBinding.bind(view)

    override fun onViewAndBindingCreated(
        view: View,
        binding: FragmentChallengePublicInfoBinding,
        savedInstanceState: Bundle?
    ) {

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        lifecycleScope.launch {
            viewModel.completeChallenge.receiveAsFlow().collect { finishChallenge ->
                val proof = ProofKind.SocialMediaLink("Public-Challenge")
                challengeService.finishChallengeWithProof(finishChallenge, proof).fold(
                    ifLeft = { err ->
                        showToast("Could not complete challenge. Please try again.")
                    },
                    ifRight = {
                        showToast("Challenge successfully completed!")
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                )

            }
        }
    }
}