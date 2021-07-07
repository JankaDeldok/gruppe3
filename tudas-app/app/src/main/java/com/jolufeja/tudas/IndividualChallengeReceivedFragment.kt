package com.jolufeja.tudas

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException


class IndividualChallengeReceivedFragment : Fragment(R.layout.fragment_challenge_received_info) {

    private lateinit var filePhoto: File
    private val REQUEST_CODE_CAMERA = 1
    private val REQUEST_CODE_IMAGE = 2
    private lateinit var takenImage: Bitmap;
    private lateinit var viewImage: ImageView;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewImage = view.findViewById<View>(R.id.imageView) as ImageView
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
            getImage();
        }

        // Listener for opening camera button
        openCamera.setOnClickListener {
            getCamera();
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

    private fun getCamera(){
        if ((activity as MainActivity?)!!.hasNoPermissions()) {
            (activity as MainActivity?)!!.requestPermission()
        }
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            takePhotoIntent,
            REQUEST_CODE_CAMERA
        );
    }

    private fun getImage(){
        if ((activity as MainActivity?)!!.hasNoPermissions()) {
            (activity as MainActivity?)!!.requestPermission()
        }
        val fileIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileIntent.type = "image/*";
            startActivityForResult(fileIntent, REQUEST_CODE_IMAGE);
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK){
            takenImage = data?.extras?.get("data") as Bitmap
            viewImage.setImageBitmap(takenImage);
        }
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            viewImage.setImageURI(data?.data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



}