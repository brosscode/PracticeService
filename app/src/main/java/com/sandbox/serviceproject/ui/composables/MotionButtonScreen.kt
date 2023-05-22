package com.sandbox.serviceproject.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sandbox.serviceproject.R
import com.sandbox.serviceproject.viewmodel.MotionButtonStates
import com.sandbox.serviceproject.viewmodel.MotionButtonStates.Companion.toButtonText
import com.sandbox.serviceproject.viewmodel.MotionViewModel

@Composable
fun MotionGameScreen(
    viewModelState: MotionViewModel.MotionState,
    onButtonClicked: () -> Unit?
) {
    when (viewModelState) {
        is MotionViewModel.MotionState.Idle -> LoadedGameScreen(
            state = viewModelState,
            onButtonClicked = onButtonClicked
        )
    }
}

@Composable
fun LoadedGameScreen(
    state: MotionViewModel.MotionState.Idle,
    onButtonClicked: () -> Unit?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = when {
                state.buttonState == MotionButtonStates.START -> stringResource(id = R.string.motion_button_title_start)
                state.countdownTime != 0 && state.buttonState != MotionButtonStates.RESTART -> stringResource(
                    id = R.string.motion_button_countdown,
                    state.countdownTime
                )
                else -> state.elapsedTime
            },
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center

        )
        if (state.buttonState == MotionButtonStates.RESUME ||
            state.buttonState == MotionButtonStates.PAUSE && state.countdownTime > 0
        ) {
            Text(
                text = state.elapsedTime,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center

            )
        }
        Button(
            modifier = Modifier.padding(8.dp),
            enabled = state.buttonEnabled,
            onClick = { onButtonClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = state.buttonState.toButtonText(LocalContext.current),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
