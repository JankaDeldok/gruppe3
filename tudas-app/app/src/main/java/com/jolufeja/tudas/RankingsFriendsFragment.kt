package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.data.RankingItem
import java.util.ArrayList


class RankingsFriendsFragment : Fragment(R.layout.fragment_rankings_friends) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfRankings: ArrayList<RankingItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..20) {
            val rank = RankingItem()
            rank.id = i
            rank.ranking = i + 1
            rank.name = "Marc"
            rank.points = 100000 / (i+1)
            if(i === 5){
                rank.rankingType = 1
            } else {
                rank.rankingType = 0
            }
            listOfRankings.add(rank)
        }

        listOfRankings.forEach {
            finalList.add(it)
        }

        mRecyclerView = view.findViewById(R.id.rankings_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager


        // Add Adapter so cards will be displayed
        mAdapter =
            context?.let {
                RecycleViewAdapter(
                    it,
                    finalList,
                    0,
                    0,
                    0,
                    R.layout.card_rankings_friends,
                    0,
                    0
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter
    }
}