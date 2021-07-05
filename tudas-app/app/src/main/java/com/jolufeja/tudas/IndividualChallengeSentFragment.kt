package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.nullable
import com.jolufeja.navigation.NavigationEventPublisher
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentChallengeSentInfoBinding
import com.jolufeja.tudas.service.challenges.ChallengeService
import com.jolufeja.tudas.service.challenges.InitialChallenge
import kotlinx.coroutines.launch
import java.time.LocalDate

private val DefaultDueDate = LocalDate.now().plusDays(1)

class IndividualChallengeSentViewModel(
    private val navigator: NavigationEventPublisher,
    private val challengeService: ChallengeService
) : ViewModel() {

    val challengeName: MutableLiveData<String> = MutableLiveData("")
    val creatorName: MutableLiveData<String> = MutableLiveData("Peter")
    val description: MutableLiveData<String> = MutableLiveData("")
    val dueDate: MutableLiveData<LocalDate> = MutableLiveData(DefaultDueDate)
    val reward: MutableLiveData<String> = MutableLiveData("")
    val isPublic: MutableLiveData<Boolean> = MutableLiveData(false)

    fun createChallenge() {
        viewModelScope.launch {
            nullable {
                val initialChallenge = InitialChallenge(
                    challengeName.value.bind(),
                    creatorName.value.bind(),
                    description.value.bind(),
                    dueDate.value.bind(),
                    reward.value.bind(),
                    isPublic.value.bind()
                )

                challengeService.createChallenge(initialChallenge).fold(
                    ifLeft = {
                        Log.d("IndividualChallengeSentViewModel", "Challenge creation failed: $it")
                    },
                    ifRight = { /* TODO: where to go from here? */ }
                )
            }

        }
    }

}

class IndividualChallengeSentFragment :
    DataBoundFragment<IndividualChallengeSentViewModel, FragmentChallengeSentInfoBinding>(
        R.layout.fragment_challenge_sent_info,
        IndividualChallengeSentViewModel::class,
        BR.challengeSentViewModel
    ) {

    override fun createBinding(view: View): FragmentChallengeSentInfoBinding =
        FragmentChallengeSentInfoBinding.bind(view)
}

class IndividualChallengeSentFragment2 : Fragment(R.layout.fragment_challenge_sent_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Back Button
        var backButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        // Title
        var title: EditText = view.findViewById<View>(R.id.challenge_title) as EditText

        // To who
        var receiver: Spinner = view.findViewById<View>(R.id.challenge_receiver) as Spinner

        // toggle Show groups only
        var groupsOnlyToggle: SwitchCompat = view.findViewById<View>(R.id.show_groups_only_switch) as SwitchCompat

        // Challenge description
        var description: EditText = view.findViewById<View>(R.id.challenge_description) as EditText

        // Reward
        var reward: EditText = view.findViewById<View>(R.id.challenge_reward) as EditText


        // Difficulty
        var time: EditText = view.findViewById<View>(R.id.challenge_time) as EditText

        // Send challenge Button
        var challengeButton: Button = view.findViewById<View>(R.id.challenge) as Button

        // Listener for Back Button to close fragment
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }

        // Listener for challenge completed button
        challengeButton.setOnClickListener {
            // TO DO
        }
    }
}