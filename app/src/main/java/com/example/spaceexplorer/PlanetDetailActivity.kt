package com.example.spaceexplorer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class PlanetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet_detail)

        // Get the data passed from MainActivity
        val planetName = intent.getStringExtra("PLANET_NAME")
        val textView = findViewById<TextView>(R.id.tv_planet_detail)
        textView.text = getPlanetDetails(planetName)

        // Request notification permission if needed (for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            } else {
                createNotification(planetName)
            }
        } else {
            // Create and show the notification
            createNotification(planetName)
        }
    }

    private fun getPlanetDetails(planetName: String?): String {
        return when (planetName) {
            "Mercury" -> "Mercury is the smallest planet in our solar system."
            "Venus" -> "Venus is the hottest planet in our solar system."
            "Earth" -> "Earth is the only planet known to support life."
            "Mars" -> "Mars is known as the Red Planet due to its reddish appearance."
            "Jupiter" -> "Jupiter is the largest planet in our solar system."
            "Saturn" -> "Saturn is famous for its beautiful ring system."
            "Uranus" -> "Uranus rotates on its side, making it unique among the planets."
            "Neptune" -> "Neptune is known for its strong winds, the fastest in the solar system."
            else -> "Unknown planet."
        }
    }

    private fun createNotification(planetName: String?) {
        Log.d("Notification", "Creating notification...")

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel, required for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "planet_channel",
                "Planet Information Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = ContextCompat.getColor(this@PlanetDetailActivity, R.color.blue)
                enableLights(true)
                enableVibration(true)
            }
            Log.d("Notification", "Creating notification channel...")
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(this, "planet_channel")
            .setSmallIcon(R.drawable.ic_space)  // Set your own small icon here
            .setContentTitle("Planet Information")
            .setContentText("$planetName: ${getPlanetDetails(planetName)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLights(ContextCompat.getColor(this, R.color.blue), 1000, 1000)
            .setVibrate(longArrayOf(0, 500, 1000))
            .setAutoCancel(true)

        Log.d("Notification", "Displaying notification...")
        notificationManager.notify(1, builder.build())
    }

    // Handle permission request result (for Android 13+)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, create the notification
            val planetName = intent.getStringExtra("PLANET_NAME")
            createNotification(planetName)
        } else {
            Log.d("Notification", "Notification permission denied.")
        }
    }
}