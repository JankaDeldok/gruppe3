package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.computations.either
import arrow.core.identity
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.FeedItem
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.data.RankingItem
import com.jolufeja.tudas.service.user.Ranking
import com.jolufeja.tudas.service.user.User
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.ArrayList


class RankingsWorldFragment(
    private val userService: UserService
) : Fragment(R.layout.fragment_rankings_world){
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfRankings: ArrayList<RankingItem> = ArrayList()
    private var finalList: MutableList<ListItem> = mutableListOf()

    private suspend fun buildRankingsList() = flow<List<ListItem>> {
        either<CommonErrors, Unit> {
            emit(emptyList())

            val feedElements = userService
                .getPublicRanking()
                .bind()
                .toRankingListItems()

            emit(feedElements)
        }.fold(
            ifLeft = { emit(emptyList()) },
            ifRight = ::identity
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        //adding items in list
//        for (i in 0..20) {
//            val rank = RankingItem()
//            rank.id = i
//            rank.ranking = i + 1
//            rank.name = "Marc"
//            rank.points = 100000 / (i+1)
//            if(i === 5){
//                rank.rankingType = 1
//            } else {
//                rank.rankingType = 0
//            }
//            listOfRankings.add(rank)
//        }
//
//        listOfRankings.forEach {
//            finalList.add(it)
//        }

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
                    0,
                    0
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter

        lifecycleScope.launch {
            buildRankingsList().collect { rankings ->
                finalList = rankings.toMutableList()
                (mAdapter as? RecycleViewAdapter)?.refreshData(rankings)
                mAdapter?.notifyDataSetChanged()
            }
        }
    }
}


fun List<Ranking>.toRankingListItems(): List<ListItem> = mapIndexed { i, entry ->
    RankingItem().apply {
        id = i
        ranking = i + 1
        name = entry.name
        points = entry.points
    }
}
