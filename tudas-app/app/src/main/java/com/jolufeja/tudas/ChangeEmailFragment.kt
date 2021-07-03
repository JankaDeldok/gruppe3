package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class ChangeEmailFragment : Fragment(R.layout.fragment_change_email) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Save Button
        var saveButton: Button = view.findViewById<View>(R.id.save) as Button

        // Back Button
        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        // Listener for Back Button to close fragment
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}