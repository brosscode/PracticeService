package com.sandbox.serviceproject.ui

object UtilFunctions {
    fun scoreToTime(time:Long):String{
        return String.format("%02d:%02d", time/60, time%60)
    }

}