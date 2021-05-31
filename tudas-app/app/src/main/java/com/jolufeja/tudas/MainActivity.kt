package com.jolufeja.tudas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.core.KoinExperimentalAPI

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @KoinExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }
}