package com.example.my_mountain.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.my_mountain.R

class StopwatchService : Service() {

    private var startTime = 0L
    private var handler = Handler()
    private var updateTime = 0L

    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "StopwatchChannel"

    private val updateRunnable = object : Runnable {
        @SuppressLint("DefaultLocale", "ForegroundServiceType")
        override fun run() {
            Log.d("StopwatchService", "Runnable 실행 중")
            val elapseMillis = SystemClock.uptimeMillis() - startTime
            updateTime = elapseMillis

            val hours = (elapseMillis / (1000 * 60 * 60)).toInt()
            val minutes = (elapseMillis / (1000 * 60) % 60).toInt()
            val seconds = (elapseMillis / 1000 % 60).toInt()

            val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            Log.d("StopwatchService", "시간 업데이트: $timeString")

            val notification = buildNotification(timeString)
            startForeground(NOTIFICATION_ID, notification)
            Log.d("StopwatchService", "알림 업데이트: $timeString")

            val intent = Intent("STOPWATCH_UPDATE")
            intent.putExtra("time", timeString)
            LocalBroadcastManager.getInstance(this@StopwatchService).sendBroadcast(intent)



            handler.postDelayed(this, 1000)
        }
    }


    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("StopwatchService", "서비스 시작")
        startTime = SystemClock.uptimeMillis()
        handler.post(updateRunnable)
        startForeground(NOTIFICATION_ID, buildNotification("00:00:00"))
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("StopwatchService", "서비스 중지")
        handler.removeCallbacks(updateRunnable)
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Stopwatch Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Stopwatch Running")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.icon_location)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
