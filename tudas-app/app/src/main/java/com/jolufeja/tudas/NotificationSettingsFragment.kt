package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment

class NotificationSettingsFragment  : Fragment(R.layout.fragment_notification_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var anyNotification: Boolean = false
        var challengeReceivedNotification: Boolean = false
        var friendsRequestNotification: Boolean = false
        var challengeEndsNotification: Boolean = false

        // Allow any Notifications
        var switchNotifications: Switch = view.findViewById<View>(R.id.switchNotifications) as Switch

        // "Challenge received" switch
        var switchChallengeReceived: Switch = view.findViewById<View>(R.id.switchChallengeReceived) as Switch

        // "Challenge received" switch
        var switchFriendRequest: Switch = view.findViewById<View>(R.id.switchFriendRequest) as Switch

        // "Challenge received" switch
        var switchChallengeEnds: Switch = view.findViewById<View>(R.id.switchChallengeEnds) as Switch

        // Save Button
        var saveButton: Button = view.findViewById<View>(R.id.save) as Button

        // Back Button
        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        // Listener for any-Notifications-Button to close fragment
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            anyNotification = isChecked
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            /*if (anyNotification){
                switchChallengeReceived.visibility = View.VISIBLE
                switchFriendRequest.visibility = View.VISIBLE
                switchChallengeEnds.visibility = View.VISIBLE
            }
            else{
                switchChallengeReceived.visibility = View.INVISIBLE
                switchFriendRequest.visibility = View.INVISIBLE
                switchChallengeEnds.visibility = View.INVISIBLE
            }*/
        }

        switchChallengeReceived.setOnCheckedChangeListener { _, isChecked ->
            challengeReceivedNotification = isChecked
            val message = if (isChecked) "Switch2:ON" else "Switch2:OFF"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        switchFriendRequest.setOnCheckedChangeListener { _, isChecked ->
            friendsRequestNotification = isChecked
            val message = if (isChecked) "Switch3:ON" else "Switch3:OFF"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        switchChallengeEnds.setOnCheckedChangeListener { _, isChecked ->
            challengeEndsNotification = isChecked
            val message = if (isChecked) "Switch4:ON" else "Switch4:OFF"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener {
            //saving changed values
            (activity as MainActivity).notificationsAllowed = anyNotification
            (activity as MainActivity).challengeReceivedNotificationAllowed = challengeReceivedNotification
            (activity as MainActivity).friendsRequestNotificationAllowed = friendsRequestNotification
            (activity as MainActivity).challengeEndsNotificationAllowed = challengeEndsNotification
            requireActivity().supportFragmentManager.popBackStack();
        }

        // Listener for Back Button to close fragment
        cancelButton.setOnClickListener {
            //setting switches back to last saved state
            anyNotification = (activity as MainActivity).notificationsAllowed
            challengeReceivedNotification = (activity as MainActivity).challengeReceivedNotificationAllowed
            friendsRequestNotification = (activity as MainActivity).friendsRequestNotificationAllowed
            challengeEndsNotification = (activity as MainActivity).challengeEndsNotificationAllowed
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}