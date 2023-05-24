package com.sandbox.serviceproject.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandbox.serviceproject.R
import com.sandbox.serviceproject.domain.GetScoresUseCase
import com.sandbox.serviceproject.domain.Score
import com.sandbox.serviceproject.domain.UpdateScoresUseCase
import com.sandbox.serviceproject.service.MotionService.Companion.START_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MotionViewModel @Inject constructor(
    val getScoresUseCase: GetScoresUseCase,
    val updateScoresUseCase: UpdateScoresUseCase,
) : ViewModel() {

    private var _motionState: MutableStateFlow<MotionState> = MutableStateFlow(MotionState.Idle())
    val state = _motionState.asStateFlow()

    sealed interface MotionState {
        data class Idle(
            val buttonState: MotionButtonStates = MotionButtonStates.START,
            val elapsedTime: Long = 0,
            val countdownTime: Int = START_DELAY,
            val buttonEnabled: Boolean = true,
        ) : MotionState

        data class ScoreBoard(
            val scores: List<Score> = listOf()
        ) : MotionState
    }

    fun updateElapsedTime(elapsedTime: Long) {
        val state = _motionState.value
        if (state is MotionState.Idle) {
            _motionState.value = state.copy(elapsedTime = elapsedTime, buttonEnabled = true)
        }
    }

    fun endGame(elapsedTime: Long) {
        val state = _motionState.value
        if (state is MotionState.Idle) {
            _motionState.value = state.copy(
                elapsedTime = elapsedTime,
                countdownTime = START_DELAY,
                buttonState = MotionButtonStates.RESTART,
                buttonEnabled = true,
            )
        }
        viewModelScope.launch {
            updateScoresUseCase(Score(elapsedTime, System.currentTimeMillis()))
        }
    }

    fun updateCountdown() {
        val state = _motionState.value
        if(state is MotionState.Idle){
            _motionState.value = state.copy(
                countdownTime = state.countdownTime - 1,
                buttonEnabled = false,
            )
        }
    }


    fun startMotionGame() {
        val state = _motionState.value
        if(state is MotionState.Idle){
            _motionState.value = state.copy(
                buttonState = MotionButtonStates.PAUSE,
                buttonEnabled = false,
            )
        }
    }

    fun pauseMotionGame() {
        val state = _motionState.value
        if(state is MotionState.Idle){
            _motionState.value = state.copy(
                buttonState = MotionButtonStates.RESUME,
                countdownTime = START_DELAY,
                buttonEnabled = true,
            )
        }
    }

    fun scoreBoardPressed() {
        viewModelScope.launch {
            _motionState.value = MotionState.ScoreBoard(getScoresUseCase())
        }
    }

    fun scoreBoardBackPressed() {
        _motionState.value = MotionState.Idle()
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
