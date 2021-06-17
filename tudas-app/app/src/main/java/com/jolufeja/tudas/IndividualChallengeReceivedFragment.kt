package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class IndividualChallengeReceivedFragment : Fragment(R.layout.fragment_challenge_received_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Back Button
        var backButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        // From who
        var title: TextView = view.findViewById<View>(R.id.challenge_title) as TextView

        // From who
        var author: TextView = view.findViewById<View>(R.id.challenge_author) as TextView

        // Challenge description
        var description: TextView = view.findViewById<View>(R.id.challenge_description) as TextView

        // Reward
        var reward: TextView = view.findViewById<View>(R.id.challenge_reward) as TextView

        // Difficulty
        var difficulty: TextView = view.findViewById<View>(R.id.challenge_difficulty) as TextView

        // Time Remaining
        var timeRemaining: TextView = view.findViewById<View>(R.id.challenge_time_left) as TextView

        // Location
        var location: TextView = view.findViewById<View>(R.id.challenge_location) as TextView

        // Add Proof File Button
        var addFile: ImageView = view.findViewById<View>(R.id.add_file) as ImageView

        // Open Camera Button
        var openCamera: ImageView = view.findViewById<View>(R.id.open_camera) as ImageView

        // Button to validate the challenge completion by a third person
        var sendToSomeone: ImageView = view.findViewById<View>(R.id.send_to_someone) as ImageView

        // Button challenge completed
        var challengeCompletedButton: Button = view.findViewById<View>(R.id.completed) as Button

        // Button challenge give up
        var challengeGiveUpButton: Button = view.findViewById<View>(R.id.give_up) as Button

        // Listener for Back Button to close fragment
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }

        // Listener for adding file button
        addFile.setOnClickListener {
            // TO DO
        }

        // Listener for opening camera button
        openCamera.setOnClickListener {
            // TO DO
        }

        // Listener for challenge completed button
        sendToSomeone.setOnClickListener {
            // TO DO
        }

        // Listener for challenge completed button
        challengeCompletedButton.setOnClickListener {
           // TO DO
        }

        // Listener for challenge give up button
        challengeGiveUpButton.setOnClickListener {
            // TO DO
        }
    }
}