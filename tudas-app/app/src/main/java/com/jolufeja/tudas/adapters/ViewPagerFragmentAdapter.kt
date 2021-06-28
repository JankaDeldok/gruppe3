package com.jolufeja.tudas.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jolufeja.tudas.ChallengesPublicFragment
import com.jolufeja.tudas.ChallengesReceivedFragment
import com.jolufeja.tudas.ChallengesSentFragment

// Adapter to create the three challenge fragments
class ViewPagerFragmentAdapter(fragment: Fragment, private val firstPage: Fragment, private val secondPage: Fragment, private val thirdPage: Fragment, private val defaultPage: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3;

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                firstPage;
            }
            1 -> {
                secondPage;
            }
            2 -> {
                thirdPage;
            }
            else -> defaultPage;
        }
    }
}