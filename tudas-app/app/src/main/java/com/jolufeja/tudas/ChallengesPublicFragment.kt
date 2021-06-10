package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChallengesPublicFragment : Fragment(R.layout.fragment_challenges_public) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfChallenges: ArrayList<Challenges> = ArrayList()
    private var createChallengeButton: Button? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..10) {
            val challenges = Challenges()
            challenges.id = i
            challenges.title = "Challenge $i"
            challenges.author = "Max Mustermann"
            challenges.description =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sagittis at justo a volutpat. In sed leo vel ipsum egestas mattis vitae eget lorem."
            challenges.points = 100
            challenges.timeLeft = 200
            listOfChallenges!!.add(challenges)
        }
        mRecyclerView = view.findViewById(R.id.challenges_public_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Add Adapter so cards will be displayed
        mAdapter = ChallengesRecycleViewAdapter(
            listOfChallenges,
            R.layout.card_challenges_public
        ) { item ->
            // Open New Fragment
            val individualChallengePublicFragment = IndividualChallengePublicFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                individualChallengePublicFragment
            )
            transaction.addToBackStack("challenge_sent_info")
            transaction.commit()
            item.id?.let { Log.d("TAG", it.toString()) }
        }
        mRecyclerView!!.adapter = mAdapter

        // Handle Create Challenge Button
        createChallengeButton = view.findViewById(R.id.create_challenge_button) as Button
        createChallengeButton!!.setOnClickListener {
            // Open New Fragment
            val individualChallengePublicFragment = IndividualChallengePublicFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                individualChallengePublicFragment
            )
            transaction.addToBackStack("challenge_sent_info")
            transaction.commit()
        }
    }
}