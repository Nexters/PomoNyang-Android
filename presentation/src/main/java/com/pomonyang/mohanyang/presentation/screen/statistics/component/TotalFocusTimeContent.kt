package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnXLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import kotlin.time.Duration

@Composable
fun TotalFocusTimeContent(
    focusTime: Duration,
    modifier: Modifier = Modifier,
) {
    val hasRecord = remember(focusTime) { focusTime.inWholeMinutes > 0L }
    val backgroundColor = if (hasRecord) {
        MnTheme.backgroundColorScheme.accent1
    } else {
        MnTheme.backgroundColorScheme.secondary
    }
    val iconRes = if (hasRecord) {
        R.drawable.ic_fire
    } else {
        R.drawable.ic_bubble_ellipses
    }
    val iconTint = if (hasRecord) {
        Color.Unspecified
    } else {
        MnTheme.iconColorScheme.disabled
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(102.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(MnRadius.small),
            )
            .padding(horizontal = MnSpacing.twoXLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
        ) {
            if (hasRecord) {
                Text(
                    text = stringResource(R.string.common_time_format, focusTime.inWholeHours, focusTime.inWholeMinutes % 60),
                    style = MnTheme.typography.header3,
                    color = MnTheme.textColorScheme.inverse,
                )

                Text(
                    text = stringResource(R.string.statistics_focus_content),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.inverse,
                )
            } else {
                Text(
                    text = stringResource(R.string.statistics_non_record),
                    style = MnTheme.typography.header5,
                    color = MnTheme.textColorScheme.disabled,
                )
            }
        }

        MnXLargeIcon(
            resourceId = iconRes,
            tint = iconTint,
        )
    }
}

private class TotalFocusTimeContentPreviewParameter :
    CollectionPreviewParameterProvider<String>(
        listOf(
            "3시간 27분",
            "",
        ),
    )

@ThemePreviews
@Composable
private fun TotalFocusTImeContentPreview(
    @PreviewParameter(TotalFocusTimeContentPreviewParameter::class) focusTime: String,
) {
    MnTheme {
        TotalFocusTimeContent(
            focusTime = Duration.parse(focusTime),
        )
    }
}
