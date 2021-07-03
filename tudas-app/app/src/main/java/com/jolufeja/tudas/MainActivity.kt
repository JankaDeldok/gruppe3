package com.jolufeja.tudas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)

    private val navigationEventBus: NavigationEventBus by inject()

    @KoinExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        // toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar);
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title);
        toolbarTitle.text = "TUDAS";

        val navController = findNavController(R.id.nav_fragment).also {
            it.addOnDestinationChangedListener { _, dest, _ ->
                if (dest.id == R.id.loginFragment || dest.id == R.id.registrationFragment ) {
                    bottomNavigationView.visibility = View.GONE
                } else {

                    // Change App Title on Toolbar according to current fragment
                    when (dest.id ) {
                        R.id.challengesFragment ->  toolbarTitle.text = "Challenges";
                        R.id.homeFragment ->  toolbarTitle.text = "Home";
                        R.id.feedFragment ->  toolbarTitle.text = "Feed";
                        R.id.rankingsFragment ->  toolbarTitle.text = "Rankings";
                        R.id.profileFragment ->  toolbarTitle.text = "Profile";
                        else -> { toolbarTitle.text = "TUDAS";
                        }
                    }

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

    fun hasNoPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions,0)
    }
}