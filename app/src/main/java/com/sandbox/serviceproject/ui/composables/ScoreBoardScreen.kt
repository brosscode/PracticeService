package com.sandbox.serviceproject.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sandbox.serviceproject.ui.UtilFunctions.scoreToTime
import com.sandbox.serviceproject.ui.theme.dimens
import com.sandbox.serviceproject.viewmodel.MotionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScoreScreen(
    viewModelState: MotionViewModel.MotionState.ScoreBoard,
    onBackClicked: () -> Unit,
) {
    MainContentBackground {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            StandardText(
                modifier = Modifier
                    .weight(1f)
                    .padding(MaterialTheme.dimens.grid.x2), text = "Rank"
            )
            StandardText(
                modifier = Modifier
                    .weight(1f)
                    .padding(MaterialTheme.dimens.grid.x2), text = "Score"
            )
            StandardText(
                modifier = Modifier
                    .weight(1f)
                    .padding(MaterialTheme.dimens.grid.x2), text = "Date"
            )
        }
        viewModelState.scores.forEachIndexed {row,score ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                val date = Date(score.date)
                val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                val dateString = formatter.format(date)
                StandardText(
                    modifier = Modifier
                        .weight(1f)
                        .padding(MaterialTheme.dimens.grid.x2), text = (row+1).toString()
                )
                StandardText(
                    modifier = Modifier
                        .weight(1f)
                        .padding(MaterialTheme.dimens.grid.x2), text = scoreToTime(score.score)
                )
                StandardText(
                    modifier = Modifier
                        .weight(1f)
                        .padding(MaterialTheme.dimens.grid.x2), text = dateString
                )
            }
        }
    }
    Icon(
        modifier = Modifier
            .padding(MaterialTheme.dimens.grid.x2)
            .clickable { onBackClicked() },
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = "Back Button",
        tint = MaterialTheme.colorScheme.onPrimary
    )
}
