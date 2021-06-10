package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChallengesSentFragment : Fragment(R.layout.fragment_challenges_sent) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfChallenges: ArrayList<Challenges> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..10) {
            val challenges = Challenges()
            challenges.id = i
            challenges.title = "Challenge $i"
            challenges.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sagittis at justo a volutpat. In sed leo vel ipsum egestas mattis vitae eget lorem."
            listOfChallenges!!.add(challenges)
        }
        mRecyclerView = view.findViewById(R.id.challenges_sent_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Add Adapter so cards will be displayed
        mAdapter = ChallengesRecycleViewAdapter(listOfChallenges, R.layout.card_challenges_sent)  { item ->
            // Open New Fragment
            val individualChallengeSentFragment = IndividualChallengeSentFragment()
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(((view as ViewGroup).parent as View).id , individualChallengeSentFragment)
            transaction.addToBackStack("challenge_sent_info")
            transaction.commit()
            item.id?.let { Log.d("TAG", it.toString()) }
        }
        mRecyclerView!!.adapter = mAdapter

    }
}