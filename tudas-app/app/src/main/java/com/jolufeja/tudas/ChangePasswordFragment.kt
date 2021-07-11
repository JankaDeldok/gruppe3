package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var saveButton: Button = view.findViewById<View>(R.id.save) as Button

        saveButton.setOnClickListener {
            // save new password
            requireActivity().supportFragmentManager.popBackStack();
        }

        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }

    }
}