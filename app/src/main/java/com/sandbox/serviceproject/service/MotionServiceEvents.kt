package com.sandbox.serviceproject.service

interface MotionServiceEvents {
    var onMotionDetected: ((Long) -> Unit)?
    var onCountdown: (() -> Unit)?
    var onTimeChanged: ((Long) -> Unit)?
}

interface MotionServiceActions {
    fun start()
    fun pause()
    fun resume()
    fun end()
}
