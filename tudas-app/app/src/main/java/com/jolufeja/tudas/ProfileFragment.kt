package com.jolufeja.tudas

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val profileFragment = ProfileFragment()

        // Friends Button
        var friendsButton: Button = view.findViewById<View>(R.id.friendsButton) as Button

        // change mail Button
        var changeEmailButton: Button = view.findViewById<View>(R.id.changeEmail) as Button

        // change password Button
        var changePasswordButton: Button = view.findViewById<View>(R.id.changePassword) as Button

        // notification Button
        var notificationButton: Button = view.findViewById<View>(R.id.notificationButton) as Button

        // log out Button
        var logOutButton: Button = view.findViewById<View>(R.id.logOutButton) as Button

        // test notifications Button
        var testNotificationsButton: Button = view.findViewById<View>(R.id.testNotificationsButton) as Button

        //opens FriendsSettingsFragment when clicked, but no layout yet
        friendsButton.setOnClickListener{
            val friendsSettingsFragment = FriendsSettingsFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                friendsSettingsFragment
            )
            transaction.addToBackStack("friends_list")
            transaction.commit()
        }

        //opens ChangeEmailFragment when clicked
        changeEmailButton.setOnClickListener{
            val changeEmailFragment = ChangeEmailFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                changeEmailFragment
            )
            transaction.addToBackStack("change_email")
            transaction.commit()
        }
        //opens ChangePasswordFragment when clicked
        changePasswordButton.setOnClickListener{
            val changePasswordFragment = ChangePasswordFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                changePasswordFragment
            )
            transaction.addToBackStack("change_password")
            transaction.commit()
        }

        //opens notification menu
        notificationButton.setOnClickListener{
            val notificationSettingsFragment = NotificationSettingsFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                notificationSettingsFragment
            )
            transaction.addToBackStack("notification")
            transaction.commit()
        }

        logOutButton.setOnClickListener {
            //logging out
        }

        testNotificationsButton.setOnClickListener {
            (activity as MainActivity).sendNotification("Test Notification", "Click me to open TUDAS")
        }
    }
}