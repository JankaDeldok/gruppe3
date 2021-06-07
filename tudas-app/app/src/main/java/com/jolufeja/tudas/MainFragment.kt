package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jolufeja.authentication.AuthenticationStore
import kotlinx.coroutines.launch

class MainFragment(
    private val authenticationStore: AuthenticationStore
) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val destination = authenticationStore.retrieve()?.let {
                Log.d("MainFragment","Found authToken - proceeding to authenticated area.")
                R.id.nav_graph_authenticated
            } ?: R.id.nav_graph_unauthenticated

            findNavController().navigate(destination)
        }
    }
}