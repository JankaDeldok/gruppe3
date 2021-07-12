package com.jolufeja.tudas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment


class NotificationSettingsFragment  : Fragment(R.layout.fragment_notification_settings) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var preferences: SharedPreferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        var editor = preferences.edit()

        var anyNotification: Boolean = preferences.getBoolean("anyNotification",true)
        var challengeReceivedNotification: Boolean = preferences.getBoolean("challengeReceivedNotification",true)
        var friendsRequestNotification: Boolean = preferences.getBoolean("friendsRequestNotification",true)
        var challengeEndsNotification: Boolean = preferences.getBoolean("challengeEndsNotification",true)


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

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            anyNotification = isChecked
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
        }

        switchFriendRequest.setOnCheckedChangeListener { _, isChecked ->
            friendsRequestNotification = isChecked
        }

        switchChallengeEnds.setOnCheckedChangeListener { _, isChecked ->
            challengeEndsNotification = isChecked
        }

        saveButton.setOnClickListener {
            editor.putBoolean("anyNotification", anyNotification)
            editor.putBoolean("challengeReceivedNotification", challengeReceivedNotification)
            editor.putBoolean("friendsRequestNotification", friendsRequestNotification)
            editor.putBoolean("challengeEndsNotification", challengeEndsNotification)
            editor.apply()
            requireActivity().supportFragmentManager.popBackStack()
        }

        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}
