package com.sandbox.serviceproject.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import com.sandbox.serviceproject.MainActivity
import com.sandbox.serviceproject.R
import kotlinx.coroutines.*

class MotionService : Service(), MotionServiceActions, MotionServiceEvents {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var job: Job? = null
    private val binder = MotionServiceBinder()

    private var initialStartTimeStampMS = 0L
    private var totalCountdownTimeMS = 0L
    private var pausedTimeStampMS = 0L
    private var resumeTimeStampMS = 0L
    private var totalPausedTimeMS = 0L

    override var onMotionDetected: ((String) -> Unit)? = null
    override var onCountdown: (() -> Unit)? = null
    override var onTimeChanged: ((String) -> Unit)? = null

    inner class MotionServiceBinder : Binder() {
        fun getService(): MotionService = this@MotionService
    }

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val accelerometerListener = AccelerometerListener(
        onMovementDetected = {
            onMotionDetected?.invoke(elapsedTimeToString(getCurrentTime()))
            end()
        },
        onNoMovement = {
            onTimeChanged?.invoke(elapsedTimeToString(getCurrentTime()))
            updateNotificationChannel()
        },
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        android.util.Log.d("Service Project", "Starting Service")
        startForeground(CHANNEL_ID, buildNotification())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            accelerometerListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun start() {
        job = scope.launch {
            initialStartTimeStampMS = System.currentTimeMillis()
            var countdown = 0
            var currentDelayedTimeMS: Long
            var lastTimeSinceCountdownMS = System.currentTimeMillis()
            while (countdown < START_DELAY) {
                currentDelayedTimeMS = System.currentTimeMillis() - lastTimeSinceCountdownMS
                if (currentDelayedTimeMS >= 1000L) {
                    lastTimeSinceCountdownMS = System.currentTimeMillis()
                    totalCountdownTimeMS += currentDelayedTimeMS
                    onCountdown?.invoke()
                    countdown += 1
                }
            }
            accelerometerListener.isHandlingEvents = true
        }
    }

    override fun pause() {
        job?.cancel()
        pausedTimeStampMS = System.currentTimeMillis()
        accelerometerListener.isHandlingEvents = false
    }

    override fun resume() {
        job = scope.launch {
            resumeTimeStampMS = System.currentTimeMillis()
            totalPausedTimeMS += resumeTimeStampMS - pausedTimeStampMS
            var countdown = 0
            var currentDelayedTimeMS: Long
            var lastTimeSinceCountdownMS = System.currentTimeMillis()
            while (countdown < START_DELAY) {
                currentDelayedTimeMS = System.currentTimeMillis() - lastTimeSinceCountdownMS
                if (currentDelayedTimeMS >= 1000L) {
                    lastTimeSinceCountdownMS = System.currentTimeMillis()
                    totalCountdownTimeMS += currentDelayedTimeMS
                    onCountdown?.invoke()
                    countdown += 1
                }
            }
            accelerometerListener.isHandlingEvents = true
        }
    }

    override fun end() {
        job?.cancel()
        accelerometerListener.isHandlingEvents = false
        initialStartTimeStampMS = 0L
        pausedTimeStampMS = 0L
        resumeTimeStampMS = 0L
        totalPausedTimeMS = 0L
        totalCountdownTimeMS = 0L
    }

    private fun buildNotification(currentTime: String = "") =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(getString(R.string.channel_name))
            .setContentText(
                currentTime.takeIf { it.isNotEmpty() } ?: getString(R.string.channel_description)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

    private fun updateNotificationChannel() =
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
            CHANNEL_ID,
            buildNotification(elapsedTimeToString(getCurrentTime()))
        )

    private fun elapsedTimeToString(timeSinceStart: Long): String {
        return DateUtils.formatElapsedTime(timeSinceStart)
    }

    private fun getCurrentTime(): Long = if (pausedTimeStampMS == 0L) {
        (System.currentTimeMillis() - (initialStartTimeStampMS + totalCountdownTimeMS)) / 1000L
    } else ((System.currentTimeMillis() - (initialStartTimeStampMS + totalCountdownTimeMS)) -
            totalPausedTimeMS) / 1000L

    companion object {
        // Tolerance for ignored movement
        const val TOLERANCE = 0.1f
        const val GRAVITY = 9.8f

        // Delay for start countdown
        const val START_DELAY = 5

        // Channel Name
        const val NOTIFICATION_CHANNEL = "motion_channel"

        // Channel ID
        const val CHANNEL_ID = 12735
    }
}
