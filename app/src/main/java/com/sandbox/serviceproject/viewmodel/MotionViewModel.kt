package com.sandbox.serviceproject.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sandbox.serviceproject.R
import com.sandbox.serviceproject.service.MotionService.Companion.START_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MotionViewModel @Inject constructor() : ViewModel() {

    private var _motionState = MutableStateFlow(MotionState.Idle())
    val state = _motionState.asStateFlow()

    sealed interface MotionState {
        data class Idle(
            val buttonState: MotionButtonStates = MotionButtonStates.START,
            val elapsedTime: String = "",
            val countdownTime: Int = START_DELAY,
            val buttonEnabled: Boolean = true,
        ) : MotionState
    }

    fun updateElapsedTime(elapsedTime: String) {
        _motionState.value = _motionState.value.copy(
            elapsedTime = elapsedTime,
            buttonEnabled = true,
        )
    }

    fun endGame(elapsedTime: String) {
        _motionState.value = _motionState.value.copy(
            elapsedTime = elapsedTime,
            countdownTime = START_DELAY,
            buttonState = MotionButtonStates.RESTART,
            buttonEnabled = true,
        )
    }

    fun updateCountdown() {
        _motionState.value = _motionState.value.copy(
            countdownTime = _motionState.value.countdownTime - 1,
            buttonEnabled = false,
        )
    }


    fun startMotionGame() {
        _motionState.value = _motionState.value.copy(
            buttonState = MotionButtonStates.PAUSE,
            buttonEnabled = false,
        )
    }

    fun pauseMotionGame() {
        _motionState.value = _motionState.value.copy(
            buttonState = MotionButtonStates.RESUME,
            countdownTime = START_DELAY,
            buttonEnabled = true,
        )
    }
}

enum class MotionButtonStates {
    START,
    RESTART,
    PAUSE,
    RESUME;

    companion object {
        fun MotionButtonStates.toButtonText(context: Context) = when (this) {
            START -> context.getString(R.string.motion_button_start)
            RESTART -> context.getString(R.string.motion_button_restart)
            PAUSE -> context.getString(R.string.motion_button_pause)
            RESUME -> context.getString(R.string.motion_button_resume)
        }
    }
}
