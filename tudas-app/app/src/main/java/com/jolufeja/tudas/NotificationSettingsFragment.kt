package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment

class NotificationSettingsFragment  : Fragment(R.layout.fragment_notification_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var anyNotification: Boolean = (activity as MainActivity).notificationsAllowed
        var challengeReceivedNotification: Boolean = (activity as MainActivity).challengeReceivedNotificationAllowed
        var friendsRequestNotification: Boolean = (activity as MainActivity).friendsRequestNotificationAllowed
        var challengeEndsNotification: Boolean = (activity as MainActivity).challengeEndsNotificationAllowed

        var switchNotifications: Switch = view.findViewById<View>(R.id.switchNotifications) as Switch
        switchNotifications.isChecked = anyNotification

        var switchChallengeReceived: Switch = view.findViewById<View>(R.id.switchChallengeReceived) as Switch
        if (!anyNotification) {
            switchChallengeReceived.isChecked = false
            switchChallengeReceived.visibility = View.GONE
        }
        else{
            switchChallengeReceived.visibility = View.VISIBLE
            switchChallengeReceived.isChecked = challengeReceivedNotification
        }

        var switchFriendRequest: Switch = view.findViewById<View>(R.id.switchFriendRequest) as Switch
        if (!anyNotification) {
            switchFriendRequest.isChecked = false
            switchFriendRequest.visibility = View.GONE
        }
        else{
            switchFriendRequest.visibility = View.VISIBLE
            switchFriendRequest.isChecked = friendsRequestNotification
        }

        var switchChallengeEnds: Switch = view.findViewById<View>(R.id.switchChallengeEnds) as Switch
        if (!anyNotification) {
            switchChallengeEnds.isChecked = false
            switchChallengeEnds.visibility = View.GONE
        }
        else{
            switchChallengeEnds.visibility = View.VISIBLE
            switchChallengeEnds.isChecked = challengeEndsNotification
        }

        var saveButton: Button = view.findViewById<View>(R.id.save) as Button

        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        // Listener for any-Notifications-Button to close fragment
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            anyNotification = isChecked
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (!isChecked){
                switchFriendRequest.isChecked = false
                switchChallengeReceived.isChecked = false
                switchChallengeEnds.isChecked = false
                challengeReceivedNotification = false
                friendsRequestNotification = false
                challengeEndsNotification = false
                switchFriendRequest.visibility = View.GONE
                switchChallengeReceived.visibility = View.GONE
                switchChallengeEnds.visibility = View.GONE
            }
            else{
                switchFriendRequest.visibility = View.VISIBLE
                switchChallengeReceived.visibility = View.VISIBLE
                switchChallengeEnds.visibility = View.VISIBLE
            }
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
            //(activity as MainActivity).mySharedPreferences(anyNotification, friendsRequestNotification, challengeReceivedNotification, challengeEndsNotification)
            requireActivity().supportFragmentManager.popBackStack();
        }

        // Listener for Back Button to close fragment
        cancelButton.setOnClickListener {
            //setting switches back to last saved state
            /*anyNotification = (activity as MainActivity).notificationsAllowed
            challengeReceivedNotification = (activity as MainActivity).challengeReceivedNotificationAllowed
            friendsRequestNotification = (activity as MainActivity).friendsRequestNotificationAllowed
            challengeEndsNotification = (activity as MainActivity).challengeEndsNotificationAllowed*/
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}
