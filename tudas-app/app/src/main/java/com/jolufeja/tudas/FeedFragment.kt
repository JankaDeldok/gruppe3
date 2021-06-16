package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.FeedRecycleViewAdapter
import com.jolufeja.tudas.data.FeedItem


class FeedFragment : Fragment(R.layout.fragment_feed) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfActivites: ArrayList<FeedItem> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..10) {
            val feed = FeedItem()
            feed.id = i
            feed.text = "Challenge $i"
            feed.type = "Max Mustermann"
            listOfActivites!!.add(feed)
        }
        mRecyclerView = view.findViewById(R.id.feed_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager


        // Add Adapter so cards will be displayed
        mAdapter =
            FeedRecycleViewAdapter(listOfActivites, R.layout.card_challenges_sent) { item ->
                // Open New Fragment
                val individualChallengeSentFragment = IndividualChallengeSentFragment()
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(
                    ((view as ViewGroup).parent as View).id,
                    individualChallengeSentFragment
                )
                transaction.addToBackStack("challenge_sent_info")
                transaction.commit()
                item.id?.let { Log.d("TAG", it.toString()) }
            }
        mRecyclerView!!.adapter = mAdapter
    }

}