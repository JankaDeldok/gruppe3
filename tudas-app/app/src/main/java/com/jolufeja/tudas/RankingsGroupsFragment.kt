package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.data.RankingGroupsItem
import java.util.ArrayList


class RankingsGroupsFragment : Fragment(R.layout.fragment_rankings_groups) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfGroups: ArrayList<RankingGroupsItem> = ArrayList()
    private var finalList: ArrayList<ListItem> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //adding items in list
        for (i in 0..20) {
            val group = RankingGroupsItem()
            group.id = i
            group.name = "Group Don"
            group.size = 12
            listOfGroups.add(group)
        }

        listOfGroups.forEach {
            finalList.add(it)
        }

        mRecyclerView = view.findViewById(R.id.group_recycler_view)
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
                    R.layout.card_rankings_groups,
                ) { item ->
                    // Open New Fragment
                    val rankingsGroupFragment = RankingsGroupFragment()
                    val transaction: FragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(
                        ((view as ViewGroup).parent as View).id,
                        rankingsGroupFragment
                    )
                    transaction.addToBackStack("ranking_group_fragment")
                    transaction.commit()
                    item.id.let { Log.d("TAG",   item.  toString()) }
                }
            }
        mRecyclerView!!.adapter = mAdapter
    }
}