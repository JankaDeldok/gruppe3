package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.computations.nullable
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.FeedItem
import com.jolufeja.tudas.data.HeaderItem
import com.jolufeja.tudas.data.ListItem
import java.util.*


class FeedFragment : Fragment(R.layout.fragment_feed) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfActivities: ArrayList<FeedItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..10) {
            val feed = FeedItem()
            feed.id = i
            feed.text = "Felix sent Laura a Challenge"
            feed.type = "Sent Challenge"
            feed.date = "2020-06-21"
            listOfActivities.add(feed)
        }

        val header = HeaderItem()
        header.text = "Today"
        finalList.add(header)

        listOfActivities.forEach {
            finalList.add(it)
        }

        val header1 = HeaderItem()
        header1.text = "Yesterday"
        finalList.add(header1)

        listOfActivities.forEach {
            finalList.add(it)
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
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter
    }
}