package com.example.my_mountain.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.my_mountain.R
import com.google.android.gms.maps.model.LatLng

class LocationService : Service() {

    private lateinit var locationManager: LocationManager
    private val locationListener = MyLocationListener(this) // Context 전달

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(1, createNotification())
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, // 1초마다 업데이트
                1f, // 1미터 이상 이동 시 업데이트
                locationListener
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "LOCATION_SERVICE_CHANNEL",
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "LOCATION_SERVICE_CHANNEL")
            .setContentTitle("Location Tracking")
            .setContentText("Tracking your location")
            .setSmallIcon(R.drawable.icon_location) // Replace with your icon
            .build()
    }

    inner class MyLocationListener(private val serviceContext: Context) : LocationListener {
        override fun onLocationChanged(location: Location) {
            val intent = Intent("LOCATION_UPDATE")
            intent.putExtra("location", Location("gps").apply {
                latitude = location.latitude
                longitude = location.longitude
            })
            LocalBroadcastManager.getInstance(serviceContext).sendBroadcast(intent)
        }
    }
}
