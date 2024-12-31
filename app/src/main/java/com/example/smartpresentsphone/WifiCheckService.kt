package com.example.smartpresentsphone

import android.Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import kotlin.math.log

class WifiCheckService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val wifiCheckRunnable = object : Runnable {
        override fun run() {
            checkWifi()
            handler.postDelayed(this, 1000) // Repeat every 1 seconds
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        handler.post(wifiCheckRunnable) // Start the first check
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(wifiCheckRunnable) // Stop the handler
    }

    private fun checkWifi() {

        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid.removeSurrounding("\"")
        System.out.println(ssid)

        if (ssid == "Vladimir" || ssid == "VladimirN_5G" ||
            ssid == "VladimirN" || ssid == "Vladimir_E") {

          //  showNotification("Connected to Vladimir!")
        }
    }

    private fun startForegroundService() {
        val channelId = "WifiCheckServiceChannel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Wifi Check Service",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification for the foreground service
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Wi-Fi Check Service")
            .setContentText("Checking Wi-Fi connection...")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 and above
            val foregroundServiceType = ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            startForeground(1, notification, foregroundServiceType)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10
            startForeground(1, notification, 0)
        } else {
            // Devices below Android 10
            startForeground(1, notification)
        }
    }

    private fun showNotification(message: String) {
        val channelId = "WifiCheckServiceChannel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Wi-Fi Check")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(2, notification)
    }
}