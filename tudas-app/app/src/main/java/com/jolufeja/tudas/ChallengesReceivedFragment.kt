package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.computations.either
import arrow.core.identity
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.ChallengesItem
import com.jolufeja.tudas.data.HeaderItem
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.service.challenges.ChallengeService
import kotlinx.coroutines.flow.flow
import org.koin.android.ext.android.get


class ChallengesReceivedFragment(
    private val challengeService: ChallengeService
) : Fragment(R.layout.fragment_challenges_received) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfChallenges: ArrayList<ChallengesItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()

    private suspend fun buildChallengeList() = flow<List<ListItem>> {
        either<CommonErrors, Unit> {
            emit(emptyList())

            val openChallenges = challengeService
                .getOpenChallenges()
                .bind()
                .toChallengeListItems()

            val completedChallenges = challengeService
                .getFinishedChallenges()
                .bind()
                .toChallengeListItems()

            val combined = listOf(HeaderItem("Public Challenges")) + openChallenges +
                    listOf(HeaderItem("Completed")) + completedChallenges

            emit(combined)
        }.fold(
            ifLeft = { emit(emptyList()) },
            ifRight = ::identity
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..10) {
            val challenges = ChallengesItem()
            challenges.id = i
            challenges.title = "Challenge $i"
            challenges.author = "Max Mustermann"
            challenges.description =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sagittis at justo a volutpat. In sed leo vel ipsum egestas mattis vitae eget lorem."
            challenges.reward = "5 Kugeln Eis"
            challenges.points = 100
            challenges.timeLeft = 200
            listOfChallenges.add(challenges)
        }
        val header = HeaderItem()
        header.text = "Open"
        finalList.add(header)

        listOfChallenges.forEach {
            finalList.add(it)
        }

        val header1 = HeaderItem()
        header1.text = "Completed"
        finalList.add(header1)

        listOfChallenges.forEach {
            finalList.add(it)
        }


        mRecyclerView = view.findViewById(R.id.challenges_received_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Add Adapter so cards will be displayed
        mAdapter = context?.let {
            RecycleViewAdapter(
                it,
                finalList,
                R.layout.card_challenges_received,
                R.layout.card_header,
                0,
                0,
                0,
                0
            ) { item ->
                // Open New Fragment
                val individualChallengeReceivedFragment = IndividualChallengeReceivedFragment()
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(
                    ((view as ViewGroup).parent as View).id,
                    individualChallengeReceivedFragment
                )
                transaction.addToBackStack("challenge_received_info")
                transaction.commit()
                item.id.let { Log.d("TAG", it.toString()) }
            }
        }
        mRecyclerView!!.adapter = mAdapter
    }
}