package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import arrow.core.computations.nullable
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.navigation.NavigationEventPublisher
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentChallengeSentInfoBinding
import com.jolufeja.tudas.service.challenges.ChallengeService
import com.jolufeja.tudas.service.challenges.InitialChallenge
import com.jolufeja.tudas.service.user.FriendEntry
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

private val DefaultDueDate = LocalDate.now().plusDays(1)

class IndividualChallengeSentViewModel(
    private val navigator: NavigationEventPublisher,
    private val challengeService: ChallengeService,
    private val authenticationService: UserAuthenticationService
) : ViewModel() {

    val challengeName: MutableLiveData<String> = MutableLiveData("")
    val creatorName: MutableLiveData<String> = MutableLiveData("")
    val description: MutableLiveData<String> = MutableLiveData("")
    val dueDate: MutableLiveData<String> = MutableLiveData("")
    val reward: MutableLiveData<String> = MutableLiveData("")
    val isPublic: MutableLiveData<Boolean> = MutableLiveData(false)
    val addressedTo: MutableLiveData<String> = MutableLiveData("")
    var recipient: String? = null


    val challengeCreated: Channel<Unit> = Channel()


    fun createChallenge() {
        viewModelScope.launch {
            nullable {
                val initialChallenge = InitialChallenge(
                    challengeName.value.bind(),
                    authenticationService.authentication.await().user.name,
                    description.value.bind(),
                    LocalDate.now().plusDays(3),
                    reward.value.bind(),
                    recipient.bind()
                )

                challengeService.createChallenge(initialChallenge).fold(
                    ifLeft = {
                        Log.d("IndividualChallengeSentViewModel", "Challenge creation failed: $it")
                    },
                    ifRight = { challengeCreated.trySend(Unit) }
                )
            }
        }
    }
}

class IndividualChallengeSentFragment(
    private val userService: UserService
) :
    DataBoundFragment<IndividualChallengeSentViewModel, FragmentChallengeSentInfoBinding>(
        R.layout.fragment_challenge_sent_info,
        IndividualChallengeSentViewModel::class,
        BR.challengeSentViewModel
    ) {

    private val friends = flow {
        either<CommonErrors, List<FriendEntry>> {
            userService.getFriendsOfCurrentUser().bind().also { emit(it) }
        }.fold(
            ifLeft = { Log.d("ERROR", it.toString()) },
            ifRight = { }
        )
    }

    override fun createBinding(view: View): FragmentChallengeSentInfoBinding =
        FragmentChallengeSentInfoBinding.bind(view)

    override fun onViewAndBindingCreated(
        view: View,
        binding: FragmentChallengeSentInfoBinding,
        savedInstanceState: Bundle?
    ) {

        lifecycleScope.launchWhenCreated {
            friends.collect { friendList ->
                binding.challengeReceiver.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    friendList.map { it.name }
                )
            }

            viewModel.challengeCreated.receiveAsFlow().collect {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }


        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.showGroupsOnlySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.challengeReceiver.isEnabled = isChecked
            when {
                isChecked -> viewModel.recipient = "Friends"
                binding.challengeReceiver.adapter.count > 0 -> {
                    viewModel.recipient = binding.challengeReceiver.adapter.getItem(0).toString()
                }
            }
        }

        val itemListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.recipient = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.challengeReceiver.onItemSelectedListener = itemListener
    }
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
        var groupsOnlyToggle: SwitchCompat =
            view.findViewById<View>(R.id.show_groups_only_switch) as SwitchCompat

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