package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment

class AddFriendFragment : Fragment(R.layout.fragment_add_friend) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var newFriendTextField : EditText = view.findViewById<View>(R.id.newFriend) as EditText

        var addButton: Button = view.findViewById<View>(R.id.add) as Button
        
        addButton.setOnClickListener {
            if (newFriendTextField.text.isEmpty()){
                Toast.makeText(requireContext(), "Please enter a username", Toast.LENGTH_SHORT).show()            }
            else{
                //look in database for the entered username (newFriendTextField.text.toString()) and add to friends list
                requireActivity().supportFragmentManager.popBackStack();
            }
        }

        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        // Listener for Back Button to close fragment
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}