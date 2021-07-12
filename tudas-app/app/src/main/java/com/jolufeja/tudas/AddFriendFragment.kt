package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jolufeja.tudas.service.user.UserService

class AddFriendFragment(
    private val userService: UserService
) : Fragment(R.layout.fragment_add_friend) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var newFriendTextField : EditText = view.findViewById<View>(R.id.newFriend) as EditText

        var addButton: Button = view.findViewById<View>(R.id.add) as Button
        
        addButton.setOnClickListener {
            if (newFriendTextField.text.isEmpty()){
                Toast.makeText(requireContext(), "Please enter a username", Toast.LENGTH_SHORT).show()            }
            else {
                lifecycleScope.launchWhenCreated {
                    val friendName = newFriendTextField.text.toString()
                    userService.addFriend(friendName).fold(
                        ifLeft = {
                                 Log.d("AddFriendFragment", "Couldn't add friend: $it")
                        },
                        ifRight = {
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    )
                }

            }
        }

        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}