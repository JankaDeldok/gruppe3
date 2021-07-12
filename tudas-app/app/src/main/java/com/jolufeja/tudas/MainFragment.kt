package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import arrow.core.computations.either
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jolufeja.authentication.AuthenticationStore
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.launch

class MainFragment(
    private val authenticationStore: AuthenticationStore,
    private val userService: UserService
) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val destination = authenticationStore.retrieve()?.let {
                Log.d("MainFragment","Found authToken - proceeding to authenticated area.")
                updatePoints()
                R.id.nav_graph_authenticated
            } ?: R.id.nav_graph_unauthenticated

            findNavController().navigate(destination)
        }
    }

    private suspend fun updatePoints() {
        either<CommonErrors, Unit> {
            val currentPoints = userService.getPointsOfCurrentUser().bind()
            (activity as MainActivity).statsChannel.trySend(currentPoints)
        }.fold(
            ifLeft = { Log.d("MainFragment", "$it")},
            ifRight = {}
        )

    }
}