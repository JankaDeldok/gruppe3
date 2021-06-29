package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment

class NotificationSettingsFragment  : Fragment(R.layout.fragment_notification_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // "Challenge received" switch
        var switchChallengeReceived: Switch = view.findViewById<View>(R.id.switchChallengeReceived) as Switch

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