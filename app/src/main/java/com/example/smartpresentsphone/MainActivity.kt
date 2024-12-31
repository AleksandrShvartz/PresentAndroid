package com.example.smartpresentsphone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.smartpresentsphone.ui.theme.SmartPresentsPhoneTheme

class MainActivity : ComponentActivity() {
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, start the service
            startWifiCheckService()
        } else {
            // Permission denied, show a message
            setContent {
                SmartPresentsPhoneTheme {
                    Text(
                        text = "Location permission is required to access Wi-Fi information.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, start the service
            startWifiCheckService()
        } else {
            // Request location permission
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Set the UI content
        setContent {
            SmartPresentsPhoneTheme {
                AppContent()
            }
        }
    }

    private fun startWifiCheckService() {
        val serviceIntent = Intent(this, WifiCheckService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    @Composable
    fun AppContent() {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Wi-Fi Check Service is running.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SmartPresentsPhoneTheme {
            AppContent()
        }
    }
}