package com.jolufeja.tudas

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jolufeja.navigation.NavigationEvent
import com.jolufeja.navigation.NavigationEventBus
import com.jolufeja.navigation.eventDrivenNavigation
import com.jolufeja.tudas.login.LoginNavigationEvents
import com.jolufeja.tudas.registration.RegistrationNavigationEvents
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

        val navController = findNavController(R.id.nav_fragment)
            .hideBottomNavigationOnInit(bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launch {
            navigationEventBus.subscribe { navController.navigationSubscriptions(it) }
        }
    }

}

private fun NavController.navigationSubscriptions(event: NavigationEvent) =
    eventDrivenNavigation(this) {
        register(RegistrationNavigationEvents.PROCEED_TO_HOME, R.id.nav_graph_authenticated)
        register(RegistrationNavigationEvents.PROCEED_TO_LOGIN, R.id.loginFragment)

        register(LoginNavigationEvents.PROCEED_TO_HOME, R.id.nav_graph_authenticated)
        register(LoginNavigationEvents.PROCEED_TO_REGISTRATION, R.id.registrationFragment)
    }(event)

private fun NavController.hideBottomNavigationOnInit(bottomNavigation: BottomNavigationView) =
    apply {
        addOnDestinationChangedListener { _, dest, _ ->
            if (dest.id == R.id.loginFragment || dest.id == R.id.registrationFragment) {
                bottomNavigation.visibility = View.GONE
            } else {
                bottomNavigation.visibility = View.VISIBLE
            }
        }
    }