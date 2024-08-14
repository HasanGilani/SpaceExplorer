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

class SpaceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_detail)

        // Get the data passed from MainActivity
        val receivedData = intent.getStringExtra("SPACE_DATA")
        val textView = findViewById<TextView>(R.id.tv_received_data)
        textView.text = receivedData

        // Request notification permission if needed (for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            } else {
                createNotification(receivedData)
            }
        } else {
            // Create and show the notification
            createNotification(receivedData)
        }
    }

    private fun createNotification(message: String?) {
        Log.d("Notification", "Creating notification...")

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel, required for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "space_channel",
                "Space Explorer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = ContextCompat.getColor(this@SpaceDetailActivity, R.color.blue)
                enableLights(true)
                enableVibration(true)
            }
            Log.d("Notification", "Creating notification channel...")
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(this, "space_channel")
            .setSmallIcon(R.drawable.ic_space)  // Set your own small icon here
            .setContentTitle("Space Explorer Notification")
            .setContentText(message ?: "Welcome to Space Explorer!")
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
            val receivedData = intent.getStringExtra("SPACE_DATA")
            createNotification(receivedData)
        } else {
            Log.d("Notification", "Notification permission denied.")
        }
    }
}