package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import arrow.core.computations.either
import arrow.core.identity
import com.jolufeja.authentication.FeedEntry
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.FeedItem
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate


class FeedFragment(
    private val userService: UserService
) : Fragment(R.layout.fragment_feed) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfActivities: ArrayList<FeedItem> = ArrayList()
    private var finalList: MutableList<ListItem> = mutableListOf()

    private suspend fun buildFeedList() = flow<List<ListItem>> {
        either<CommonErrors, Unit> {
            emit(emptyList())

            val feedElements = userService
                .getFeed()
                .bind()
                .toFeedListItems()

            emit(feedElements)
        }.fold(
            ifLeft = {
                Log.d("FeedFragment", it.toString())
                showToast("Could not fetch feed. Please try again.")
                emit(emptyList())
            },
            ifRight = ::identity
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.feed_swiperefresh)
        refreshLayout?.setOnRefreshListener {
            lifecycleScope.launch {
                buildFeedList().collect { feed ->
                    refreshLayout.isRefreshing = false
                    finalList = feed.toMutableList()
                    (mAdapter as? RecycleViewAdapter)?.refreshData(feed)
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }



        mRecyclerView = view.findViewById(R.id.feed_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager


        // Add Adapter so cards will be displayed
        mAdapter =
            context?.let {
                RecycleViewAdapter(
                    it,
                    finalList,
                    0,
                    R.layout.card_feed_header,
                    R.layout.card_feed,
                    0,
                    0,
                    0,
                    0
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter

        lifecycleScope.launch {
            buildFeedList().collect { feed ->
                finalList = feed.toMutableList()
                (mAdapter as? RecycleViewAdapter)?.refreshData(feed)
                mAdapter?.notifyDataSetChanged()
            }
        }
    }


}

private fun List<FeedEntry>.toFeedListItems(): List<ListItem> = mapIndexed { i, entry ->
    FeedItem().apply {
        id = i
        text = entry.message
        date = LocalDate.now().toString()
        type = "Sent challenge"
    }
}
