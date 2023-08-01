package com.example.soilsmetals

import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.soilsmetals.data.accessed
import com.example.soilsmetals.data.internet
import com.example.soilsmetals.ui.SoilsMetalsApp
import com.example.soilsmetals.ui.theme.SoilsMetalsTheme

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                internet = true
            }

            override fun onUnavailable() {
                super.onUnavailable()
                internet = false
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                internet = false
            }
        }
        (getSystemService(ConnectivityManager::class.java) as ConnectivityManager)
            .requestNetwork(networkRequest, networkCallback)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { mapped ->
            val results: Set<Boolean?> = mapped.keys.map { mapped[it] }.toSet()
            if (results.size != 1 || results.contains(false)) {
                this.finish()
            } else {
                accessed = true
                this.recreate()
            }
        }

        if (Build.VERSION.SDK_INT < 33) {
            if (
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    this.packageManager.getPackageInfo(
                        this.packageName,
                        PackageManager.GET_PERMISSIONS
                    ).requestedPermissions.filter {
                        this.packageManager.checkPermission(
                            it,
                            this.packageName
                        ) != PackageManager.PERMISSION_GRANTED
                    }.toTypedArray()
                )
            } else {
                accessed = true
            }
        } else {
            accessed = true
        }


        setContent {
            var darkEnabled by rememberSaveable { mutableStateOf<Boolean?>(null) }
            val systemMode = isSystemInDarkTheme()
            SoilsMetalsTheme(
                darkEnabled
            ) {
                if (accessed) {
                    SoilsMetalsApp(
                        askOrChangeTheme = { boolean ->
                            if (boolean) {
                                darkEnabled ?: systemMode
                            } else {
                                darkEnabled = darkEnabled?.not() ?: !systemMode
                                darkEnabled ?: true
                            }
                        }
                    )
                }
            }
        }
    }
}

