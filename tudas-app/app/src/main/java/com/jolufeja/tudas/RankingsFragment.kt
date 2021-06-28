package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jolufeja.tudas.adapters.ViewPagerFragmentAdapter


class RankingsFragment : Fragment(R.layout.fragment_rankings) {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null
    var tabTitles: Array<String> = arrayOf("Friends", "Groups", "World")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true);
        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // create a new tab for each rankings fragment
        for (item in tabTitles)
            tabLayout!!.addTab(tabLayout!!.newTab().setText(item))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        // create adapter
        val adapter = ViewPagerFragmentAdapter(
            this,
            RankingsFriendsFragment(),
            RankingsGroupsFragment(),
            RankingsWorldFragment(),
            RankingsFriendsFragment()
        )
        viewPager!!.adapter = adapter

        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }
}