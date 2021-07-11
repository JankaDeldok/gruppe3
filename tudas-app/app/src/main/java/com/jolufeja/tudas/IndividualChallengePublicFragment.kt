package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jolufeja.presentation.fragment.DataBoundFragment

//class IndividualChallengePublicViewModel :

//class IndividualChallengePublicFragment2 : DataBoundFragment


class IndividualChallengePublicFragment : Fragment(R.layout.fragment_challenge_public_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Back Button
        var backButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        // From who
        var title: EditText = view.findViewById<View>(R.id.challenge_title) as EditText

        // Challenge description
        var description: EditText = view.findViewById<View>(R.id.challenge_description) as EditText

        // Difficulty
        var difficulty: EditText = view.findViewById<View>(R.id.challenge_difficulty) as EditText

        // Duration
        var time: EditText = view.findViewById<View>(R.id.challenge_time) as EditText

        // Send challenge Button
        var challengeButton: Button = view.findViewById<View>(R.id.challenge) as Button

        // Listener for Back Button to close fragment
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }



        // Listener for challenge completed button
        challengeButton.setOnClickListener {
            // TO DO
        }
    }
}