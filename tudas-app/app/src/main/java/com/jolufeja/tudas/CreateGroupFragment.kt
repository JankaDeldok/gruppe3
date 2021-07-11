package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.*
import java.util.ArrayList

class CreateGroupFragment : Fragment(R.layout.fragment_create_group) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfFriends: ArrayList<CreateGroupItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Back Button
        var backButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        // Send challenge Button
        var createButton: Button = view.findViewById<View>(R.id.create_group_button) as Button

        // Listener for Back Button to close fragment
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }

        // Listener for challenge completed button
        createButton.setOnClickListener {
            // TO DO
        }

        //adding items in list
        for (i in 0..20) {
            val friend = CreateGroupItem()
            friend.id = i
            friend.text = "Marc"
            if (i === 5) {
                friend.rankingType = 1
            } else {
                friend.rankingType = 0
            }
            listOfFriends.add(friend)
        }

        listOfFriends.forEach {
            finalList.add(it)
        }

        mRecyclerView = view.findViewById(R.id.create_group_recycler_view)
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
                    0,
                    R.layout.card_friends,
                ) {
                        item ->
                    // Add to group
                }
            }
        mRecyclerView!!.adapter = mAdapter
    }
}