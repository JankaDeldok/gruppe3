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
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.data.RankingItem
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.ArrayList


class RankingsFriendsFragment(
    private val userService: UserService
) : Fragment(R.layout.fragment_rankings_friends) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfRankings: ArrayList<RankingItem> = ArrayList()
    private var finalList: MutableList<ListItem> = mutableListOf()

    private suspend fun buildRankingsList() = flow<List<ListItem>> {
        either<CommonErrors, Unit> {
            emit(emptyList())

            val feedElements = userService
                .getFriendsRanking()
                .bind()
                .toRankingListItems()

            emit(feedElements)
        }.fold(
            ifLeft = { emit(emptyList()) },
            ifRight = ::identity
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
