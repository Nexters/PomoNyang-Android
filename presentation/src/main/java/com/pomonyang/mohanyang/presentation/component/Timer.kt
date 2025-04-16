package com.pomonyang.mohanyang.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.DEFAULT_TIME
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun Timer(
    time: String,
    exceededTime: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            time.toCharArray().forEach { num ->
                Text(
                    modifier = Modifier
                        .width(if (num != ':') 31.5.dp else 16.dp)
                        .offset(y = if (num == ':') (-4).dp else 0.dp),
                    text = "$num",
                    style = MnTheme.typography.header1.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                    color = MnTheme.textColorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        if (exceededTime != DEFAULT_TIME) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                exceededTime.toCharArray().forEach { num ->
                    Text(
                        modifier = Modifier
                            .width(if (num != ':') 13.dp else 6.dp)
                            .offset(y = if (num == ':') (-2).dp else 0.dp),
                        text = "$num",
                        style = MnTheme.typography.header4.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        ),
                        color = MnTheme.backgroundColorScheme.accent1,
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    modifier = Modifier.padding(start = MnSpacing.xSmall),
                    text = stringResource(R.string.timer_exceed_time),
                    style = MnTheme.typography.header4,
                    color = MnTheme.backgroundColorScheme.accent1,
                )
            }
        }
    }
}

private class TimerPreviewParameterProvider : PreviewParameterProvider<TimerPreviewParams> {
    override val values = sequenceOf(
        TimerPreviewParams(
            time = "30:00",
            exceededTime = DEFAULT_TIME,
        ),
        TimerPreviewParams(
            time = "00:00",
            exceededTime = "00:01",
        ),
        TimerPreviewParams(
            time = "10:00",
            exceededTime = DEFAULT_TIME,
        ),
    )
}

private data class TimerPreviewParams(
    val time: String,
    val exceededTime: String,
)

@Preview(showBackground = true)
@Composable
private fun TimerPreview(
    @PreviewParameter(TimerPreviewParameterProvider::class) params: TimerPreviewParams,
) {
    Timer(
        time = params.time,
        exceededTime = params.exceededTime,
    )
}
