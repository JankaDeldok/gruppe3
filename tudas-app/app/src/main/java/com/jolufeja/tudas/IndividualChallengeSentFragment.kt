package com.jolufeja.tudas

import android.app.DatePickerDialog
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

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
    val worth: MutableLiveData<Int> = MutableLiveData(200)

    var recipient: String? = null
    var day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    var month: Int = Calendar.getInstance().get(Calendar.MONTH)
    var year: Int = Calendar.getInstance().get(Calendar.YEAR)

    fun calculateTime(): Date {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }


    val challengeCreated: Channel<Boolean> = Channel()


    fun createChallenge() {
        viewModelScope.launch {
            nullable {
                val initialChallenge = InitialChallenge(
                    challengeName.value.bind(),
                    authenticationService.authentication.await().user.name,
                    description.value.bind(),
                    calculateTime(),
                    reward.value.bind(),
                    recipient.bind(),
                    200
                )

                challengeService.createChallenge(initialChallenge).fold(
                    ifLeft = {
                        Log.d("IndividualChallengeSentViewModel", "Challenge creation failed: $it")
                        challengeCreated.trySend(false)
                    },
                    ifRight = { challengeCreated.trySend(true) }
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

            viewModel.challengeCreated.receiveAsFlow().collect { success ->
                if (success) {
                    showToast("Challenge successfully created!")
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    showToast("Could not create challenge. Please try again.")
                }

            }
        }

        binding.challengeTime.setOnClickListener { challengeTime ->
            val calendar = Calendar.getInstance()
            val picker = DatePickerDialog(requireContext())
            picker.setOnDateSetListener { view, year, month, dayOfMonth ->
                val dateString = "${dayOfMonth}.${month}.${year}"
                viewModel.day = dayOfMonth
                viewModel.month = month
                viewModel.year = year
                binding.challengeTime.setText(dateString)
            }

            picker.show()
        }


        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.showGroupsOnlySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.challengeReceiver.isEnabled = !isChecked
            when {
                isChecked -> viewModel.recipient = "public"
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
