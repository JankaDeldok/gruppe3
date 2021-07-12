package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.jolufeja.authentication.UserAuthenticationService
import com.jolufeja.tudas.service.SettingsService
import com.jolufeja.tudas.service.UserSettings
import com.jolufeja.tudas.service.UserSettings.Companion.byAuthenticatedUser

class ChangeEmailFragment(
    private val settingsService: SettingsService,
    private val authenticationService: UserAuthenticationService
) : Fragment(R.layout.fragment_change_email) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var saveButton: Button = view.findViewById<View>(R.id.save) as Button

        var cancelButton: Button = view.findViewById<View>(R.id.cancel) as Button

        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }

        saveButton.setOnClickListener {
            val newEmailAddress = view.findViewById<EditText>(R.id.newEmail)
                .text.toString()
            requireActivity().supportFragmentManager.popBackStack();
        }
    }

    suspend fun updateSettings(emailAddress: String) = with(authenticationService) {
        val userSettings = byAuthenticatedUser()
    }

}