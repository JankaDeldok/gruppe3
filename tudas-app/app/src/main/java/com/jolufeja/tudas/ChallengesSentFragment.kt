package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import arrow.core.computations.either
import arrow.core.identity
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.ChallengesItem
import com.jolufeja.tudas.data.HeaderItem
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.service.challenges.ChallengeService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get


class ChallengesSentFragment(
    private val challengeService: ChallengeService
) : Fragment(R.layout.fragment_challenges_sent) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfChallenges: ArrayList<ChallengesItem> = ArrayList()
    private var createChallengeButton: Button? = null
    private var finalList: MutableList<ListItem> = mutableListOf()

    private suspend fun buildChallengeList() = flow<List<ListItem>> {
        either<CommonErrors, Unit> {
            emit(emptyList())

            val sentChallenges = challengeService
                .getOwnCreatedChallenges()
                .bind()
                .toChallengeListItems()

            val combined = listOf(HeaderItem("Sent Challenges")) + sentChallenges

            emit(combined)
        }.fold(
            ifLeft = { emit(emptyList()) },
            ifRight = ::identity
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.sent_swiperefresh)

        refreshLayout?.setOnRefreshListener {
            lifecycleScope.launch {
                buildChallengeList().collect { challenges ->
                    refreshLayout.isRefreshing = false
                    finalList = challenges.toMutableList()
                    (mAdapter as? RecycleViewAdapter)?.refreshData(challenges)
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }

        mRecyclerView = view.findViewById(R.id.challenges_sent_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Add Adapter so cards will be displayed
        mAdapter =
            context?.let {
                RecycleViewAdapter(
                    it,
                    finalList,
                    R.layout.card_challenges_sent,
                    R.layout.card_header,
                    0,
                    0,
                    0,
                    0,
                    0
                ) { item ->
                    // Open New Fragment
                    val individualChallengeSentFragment = IndividualChallengeSentFragment(get())
                    val transaction: FragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(
                        ((view as ViewGroup).parent as View).id,
                        individualChallengeSentFragment
                    )
                    transaction.addToBackStack("challenge_sent_info")
                    transaction.commit()
                    item.id.let { Log.d("TAG", it.toString()) }
                }
            }

        mRecyclerView!!.adapter = mAdapter

        lifecycleScope.launch {
            buildChallengeList().collect { challenges ->
                finalList = challenges.toMutableList()
                (mAdapter as? RecycleViewAdapter)?.refreshData(challenges)
                mAdapter?.notifyDataSetChanged()
            }
        }

        // Handle Create Challenge Button
        createChallengeButton = view.findViewById(R.id.create_challenge_button) as Button
        createChallengeButton!!.setOnClickListener {
            // Open New Fragment
            val individualChallengeSentFragment = IndividualChallengeSentFragment(get())
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                individualChallengeSentFragment
            )
            transaction.addToBackStack("challenge_sent_info")
            transaction.commit()
        }
    }
}