package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class IndividualChallengeSentFragment : Fragment(R.layout.fragment_challenge_sent_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var button: TextView = view.findViewById<View>(R.id.back_button) as TextView
        button.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}