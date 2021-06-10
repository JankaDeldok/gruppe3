package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ChallengesFragment : Fragment(R.layout.fragment_challenges) {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null
    var tabTitles: Array<String> = arrayOf("Received", "Sent", "Public")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true);
        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // create a new tab for each challenge fragment
        for (item in tabTitles)
            tabLayout!!.addTab(tabLayout!!.newTab().setText(item))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        // create adapter
        val adapter = ChallengesViewPagerFragmentAdapter(this)
        viewPager!!.adapter = adapter

        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }

}

