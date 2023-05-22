package com.sandbox.serviceproject.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.sandbox.serviceproject.service.MotionService.Companion.NOTIFICATION_CHANNEL
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MotionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                MOTION_APP_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val MOTION_APP_NAME = "Phone Movement App"
    }
}