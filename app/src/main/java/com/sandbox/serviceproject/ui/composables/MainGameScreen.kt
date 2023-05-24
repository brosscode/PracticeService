package com.sandbox.serviceproject.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sandbox.serviceproject.R
import com.sandbox.serviceproject.ui.UtilFunctions.scoreToTime
import com.sandbox.serviceproject.ui.theme.dimens
import com.sandbox.serviceproject.viewmodel.MotionButtonStates
import com.sandbox.serviceproject.viewmodel.MotionButtonStates.Companion.toButtonText
import com.sandbox.serviceproject.viewmodel.MotionViewModel


@Composable
fun LoadedGameScreen(
    state: MotionViewModel.MotionState.Idle,
    onButtonClicked: () -> Unit,
    onScoreBoardClicked: () -> Unit,
) {
    MainContentBackground {
        StandardText(
            text = when {
                state.buttonState == MotionButtonStates.START -> stringResource(id = R.string.motion_button_title_start)
                state.countdownTime != 0 && state.buttonState != MotionButtonStates.RESTART -> stringResource(
                    id = R.string.motion_button_countdown,
                    state.countdownTime
                )

                else -> scoreToTime(state.elapsedTime)
            },
            fontSize = 20.sp,
        )
        if (state.buttonState == MotionButtonStates.RESUME ||
            state.buttonState == MotionButtonStates.PAUSE && state.countdownTime > 0
        ) {
            StandardText(
                text = scoreToTime(state.elapsedTime),
                fontSize = 20.sp,
            )
        }
        Button(
            modifier = Modifier.padding(MaterialTheme.dimens.grid.x2),
            enabled = state.buttonEnabled,
            onClick = { onButtonClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            StandardText(
                text = state.buttonState.toButtonText(LocalContext.current),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
    if (state.buttonState == MotionButtonStates.START || state.buttonState == MotionButtonStates.RESTART) {
        Button(onClick = { onScoreBoardClicked() }) {
            StandardText(text = "Score Board", color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}
