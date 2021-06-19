package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Friends Button
        var friendsButton: Button = view.findViewById<View>(R.id.friendsButton) as Button

        // Friends Button
        var changeEmailButton: Button = view.findViewById<View>(R.id.changeEmail) as Button

        // Friends Button
        var changePasswordButton: Button = view.findViewById<View>(R.id.changePassword) as Button

        // Friends Button
        var notificationButton: Button = view.findViewById<View>(R.id.notificationButton) as Button

        // Friends Button
        var logOutButton: Button = view.findViewById<View>(R.id.logOutButton) as Button

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

        //opens Password Fragment when clicked, cause notification fragment not yet implemented
        notificationButton.setOnClickListener{
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

        logOutButton.setOnClickListener {
            //logging out
        }
    }
}