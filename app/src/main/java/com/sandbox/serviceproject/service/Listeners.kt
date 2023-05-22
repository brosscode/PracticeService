package com.sandbox.serviceproject.service

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import kotlin.math.abs

class AccelerometerListener(
    var isHandlingEvents: Boolean = false,
    val onMovementDetected: () -> Unit,
    val onNoMovement: () -> Unit,
) : SensorEventListener2 {
    override fun onSensorChanged(event: SensorEvent?) {
        if (isHandlingEvents) {
            val currentEventValues = event?.values
            if (currentEventValues != null) {
                when {
                    abs(currentEventValues[0]) > MotionService.TOLERANCE -> onMovementDetected()
                    abs(currentEventValues[1]) > MotionService.TOLERANCE -> onMovementDetected()
                    abs(currentEventValues[2]) > MotionService.TOLERANCE + MotionService.GRAVITY -> onMovementDetected()
                    else -> onNoMovement()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onFlushCompleted(sensor: Sensor?) {}
}

class GyroScopeListener(
    var isHandlingEvents: Boolean = false,
    val onMovementDetected: () -> Unit,
    val onNoMovement: () -> Unit,
) : SensorEventListener2 {
    override fun onSensorChanged(event: SensorEvent?) {
        if (isHandlingEvents) {
            val currentEventValues = event?.values
            if (currentEventValues != null) {
                when {
                    currentEventValues[0] > MotionService.TOLERANCE -> onMovementDetected()
                    currentEventValues[1] > MotionService.TOLERANCE -> onMovementDetected()
                    currentEventValues[2] > MotionService.TOLERANCE + MotionService.GRAVITY -> onMovementDetected()
                    else -> onNoMovement()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onFlushCompleted(sensor: Sensor?) {}
}
