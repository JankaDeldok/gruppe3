package com.jolufeja.tudas

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// Adapter to create the three challenge fragments
class ChallengesViewPagerFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3;

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChallengesReceivedFragment()
            }
            1 -> {
                ChallengesSentFragment()
            }
            2 -> {
                ChallengesPublicFragment()
            }
            else -> ChallengesReceivedFragment()
        }
    }
}