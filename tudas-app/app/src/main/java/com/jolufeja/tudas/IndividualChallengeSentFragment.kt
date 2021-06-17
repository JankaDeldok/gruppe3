package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class IndividualChallengeSentFragment : Fragment(R.layout.fragment_challenge_sent_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Back Button
        var backButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        // Title
        var title: EditText = view.findViewById<View>(R.id.challenge_title) as EditText

        // To who
        var receiver: Spinner = view.findViewById<View>(R.id.challenge_receiver) as Spinner

        // toggle Show groups only
        var groupsOnlyToggle: Switch = view.findViewById<View>(R.id.show_groups_only_switch) as Switch

        // Challenge description
        var description: EditText = view.findViewById<View>(R.id.challenge_description) as EditText

        // Reward
        var reward: EditText = view.findViewById<View>(R.id.challenge_reward) as EditText

        // Difficulty
        var difficulty: EditText = view.findViewById<View>(R.id.challenge_difficulty) as EditText


        // Difficulty
        var time: EditText = view.findViewById<View>(R.id.challenge_time) as EditText

        // Duration
        var location: EditText = view.findViewById<View>(R.id.challenge_location) as EditText

        // Open Camera Button
        var openCamera: ImageView = view.findViewById<View>(R.id.open_camera) as ImageView

        // Send challenge Button
        var challengeButton: Button = view.findViewById<View>(R.id.challenge) as Button

        // Listener for Back Button to close fragment
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }

        // Listener for opening camera button
        openCamera.setOnClickListener {
            // TO DO
        }

        // Listener for challenge completed button
        challengeButton.setOnClickListener {
            // TO DO
        }
    }
}