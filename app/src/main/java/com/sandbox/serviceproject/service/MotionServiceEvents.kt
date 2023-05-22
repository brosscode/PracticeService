package com.sandbox.serviceproject.service

interface MotionServiceEvents {
    var onMotionDetected: ((String) -> Unit)?
    var onCountdown: (() -> Unit)?
    var onTimeChanged: ((String) -> Unit)?
}

interface MotionServiceActions {
    fun start()
    fun pause()
    fun resume()
    fun end()
}
