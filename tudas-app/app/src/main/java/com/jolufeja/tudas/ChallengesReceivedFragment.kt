package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.ChallengesRecycleViewAdapter
import com.jolufeja.tudas.data.ChallengesItem


class ChallengesReceivedFragment : Fragment(R.layout.fragment_challenges_received) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfChallenges: ArrayList<ChallengesItem> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..10) {
            val challenges = ChallengesItem()
            challenges.id = i
            challenges.title = "Challenge $i"
            challenges.author = "Max Mustermann"
            challenges.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sagittis at justo a volutpat. In sed leo vel ipsum egestas mattis vitae eget lorem."
            challenges.reward = "5 Kugeln Eis"
            challenges.points = 100
            challenges.timeLeft = 200
            listOfChallenges.add(challenges)
        }
        mRecyclerView = view.findViewById(R.id.challenges_received_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Add Adapter so cards will be displayed
        mAdapter = ChallengesRecycleViewAdapter(listOfChallenges, R.layout.card_challenges_received)  { item ->
            // Open New Fragment
            val individualChallengeReceivedFragment = IndividualChallengeReceivedFragment()
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(((view as ViewGroup).parent as View).id , individualChallengeReceivedFragment)
            transaction.addToBackStack("challenge_received_info")
            transaction.commit()
            item.id.let { Log.d("TAG", it.toString()) }
        }
        mRecyclerView!!.adapter = mAdapter

    }

}