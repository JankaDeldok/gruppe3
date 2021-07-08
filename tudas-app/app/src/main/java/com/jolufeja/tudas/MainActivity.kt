package com.jolufeja.tudas

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
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

    var notificationsAllowed = true
    var challengeReceivedNotificationAllowed = true
    var friendsRequestNotificationAllowed = true
    var challengeEndsNotificationAllowed = true

    private val CHANNEL_ID = "channel_id_test_01"
    private val notificationId = 101


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

        createNotificationChannel()

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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TUDAS Notification Channel"
            val descriptionText = "This is the Channel for all TUDAS Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title: String, description: String) {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent((pendingIntent))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        builder.setAutoCancel(true);

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

}