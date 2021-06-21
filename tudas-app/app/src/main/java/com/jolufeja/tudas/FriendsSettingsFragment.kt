package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.FeedItem
import com.jolufeja.tudas.data.FriendsItem
import com.jolufeja.tudas.data.HeaderItem
import com.jolufeja.tudas.data.ListItem
import java.util.ArrayList

class FriendsSettingsFragment  : Fragment(R.layout.fragment_friends_settings) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfActivities: ArrayList<FriendsItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*
        //adding items in list
        for (i in 0..10) {
            val friends = FriendsItem()
            friends.id = i
            friends.text = "Felix sent Laura a Challenge"
            listOfActivities.add(friends)
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



        mRecyclerView = view.findViewById(R.id.lists_recycler_view)
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
                    R.layout.card_friends_settings,
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter
         */
    }
}