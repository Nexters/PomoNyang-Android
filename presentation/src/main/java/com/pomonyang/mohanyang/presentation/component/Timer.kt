package com.pomonyang.mohanyang.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroTimerViewModel.Companion.DEFAULT_TIME
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun Timer(
    time: String,
    exceededTime: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = time,
            style = MnTheme.typography.header1,
            color = MnTheme.textColorScheme.primary
        )
        if (exceededTime != DEFAULT_TIME) {
            Text(
                text = "$exceededTime 초과",
                style = MnTheme.typography.header4,
                color = MnTheme.backgroundColorScheme.accent1
            )
        }
    }
}

private class TimerPreviewParameterProvider : PreviewParameterProvider<TimerPreviewParams> {
    override val values = sequenceOf(
        TimerPreviewParams(
            time = "25:00",
            exceededTime = DEFAULT_TIME
        ),
        TimerPreviewParams(
            time = "00:00",
            exceededTime = "5:00"
        ),
        TimerPreviewParams(
            time = "10:00",
            exceededTime = DEFAULT_TIME
        )
    )
}

private data class TimerPreviewParams(
    val time: String,
    val exceededTime: String
)

@Preview(showBackground = true)
@Composable
private fun TimerPreview(
    @PreviewParameter(TimerPreviewParameterProvider::class) params: TimerPreviewParams
) {
    Timer(
        time = params.time,
        exceededTime = params.exceededTime
    )
}
