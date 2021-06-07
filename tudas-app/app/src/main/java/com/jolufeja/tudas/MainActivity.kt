package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jolufeja.navigation.NavigationEventBus
import com.jolufeja.navigation.eventDrivenNavigation
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.core.KoinExperimentalAPI

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navigationEventBus: NavigationEventBus by inject()

    @KoinExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        val navController = findNavController(R.id.nav_fragment).also {
            it.addOnDestinationChangedListener { _, dest, _ ->
                if (dest.id == R.id.loginFragment || dest.id == R.id.registrationFragment ) {
                    bottomNavigationView.visibility = View.GONE
                } else {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launch {
            navigationEventBus.subscribe { navigationSubscriptions(navController)(it) }
        }
    }

    private fun navigationSubscriptions(navController: NavController) = eventDrivenNavigation(navController) {
        register(RegistrationNavigationEvents.PROCEED_TO_HOME, R.id.nav_graph_authenticated)
        register(RegistrationNavigationEvents.PROCEED_TO_LOGIN, R.id.loginFragment)

        register(LoginNavigationEvents.PROCEED_TO_HOME, R.id.nav_graph_authenticated)
        register(LoginNavigationEvents.PROCEED_TO_REGISTRATION, R.id.registrationFragment)
    }
}