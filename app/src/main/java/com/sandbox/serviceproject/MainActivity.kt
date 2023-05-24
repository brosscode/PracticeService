package com.sandbox.serviceproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.sandbox.serviceproject.service.MotionService
import com.sandbox.serviceproject.service.MotionService.MotionServiceBinder
import com.sandbox.serviceproject.ui.composables.LoadedGameScreen
import com.sandbox.serviceproject.ui.composables.ScoreScreen
import com.sandbox.serviceproject.ui.theme.ServiceProjectTheme
import com.sandbox.serviceproject.viewmodel.MotionButtonStates
import com.sandbox.serviceproject.viewmodel.MotionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MotionViewModel by viewModels()
    private var motionService: MotionService? = null
    private var motionBinder: MotionServiceBinder? = null
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceProjectTheme {
                val motionState by viewModel.state.collectAsState()
                when (motionState) {
                    is MotionViewModel.MotionState.Idle -> LoadedGameScreen(
                        state = motionState as MotionViewModel.MotionState.Idle,
                        onButtonClicked = { handleButtonClickEvent((motionState as MotionViewModel.MotionState.Idle).buttonState) },
                        onScoreBoardClicked = { viewModel.scoreBoardPressed() },
                    )

                    is MotionViewModel.MotionState.ScoreBoard -> ScoreScreen(motionState as MotionViewModel.MotionState.ScoreBoard) { viewModel.scoreBoardBackPressed() }
                }
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            viewModel.viewModelScope.launch {
                motionBinder = binder as MotionService.MotionServiceBinder
                motionService = motionBinder?.getService()
                motionService?.onMotionDetected = {
                    viewModel.endGame(it)
                }
                motionService?.onCountdown = {
                    viewModel.updateCountdown()
                }
                motionService?.onTimeChanged = {
                    viewModel.updateElapsedTime(it)
                }
                isBound = true
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isBound) {
            bindService()
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
    }

    private fun bindService() {
        Intent(this, MotionService::class.java).apply {
            bindService(this, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun handleButtonClickEvent(state: MotionButtonStates) = when (state) {
        MotionButtonStates.START, MotionButtonStates.RESTART -> {
            viewModel.startMotionGame()
            motionService?.start()
        }

        MotionButtonStates.PAUSE -> {
            viewModel.pauseMotionGame()
            motionService?.pause()
        }

        MotionButtonStates.RESUME -> {
            viewModel.startMotionGame()
            motionService?.resume()
        }
    }
}
