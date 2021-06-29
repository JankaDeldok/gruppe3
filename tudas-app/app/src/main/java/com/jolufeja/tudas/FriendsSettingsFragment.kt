package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.*
import java.util.ArrayList

class FriendsSettingsFragment  : Fragment(R.layout.fragment_friends_settings) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfFriends: ArrayList<FriendsItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var friends = FriendsItem()
        //adding items in list
        for (i in 0..10) {
            friends.id = i
            friends.text = "Tinkerbell"
            listOfFriends.add(friends)
        }

        listOfFriends.forEach {
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
                    0,
                    0,
                    0,
                    0,
                    R.layout.card_friends_settings
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter


        // Back Button
        var addNewFriendsButton: Button = view.findViewById<View>(R.id.add_friends_button) as Button

        // Back Button
        var cancelButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        // Listener for Back Text View to close fragment
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}