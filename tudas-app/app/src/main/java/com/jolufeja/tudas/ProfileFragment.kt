package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jolufeja.authentication.UserAuthenticationService
import kotlinx.coroutines.launch


class ProfileFragment(
    private val authenticationService: UserAuthenticationService
) : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logOut: Button = view.findViewById(R.id.button11)

        logOut.setOnClickListener {
            lifecycleScope.launch {
                authenticationService.logout()
                findNavController().navigate(R.id.nav_graph_unauthenticated)
            }
        }
    }

}