package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.time.Duration

@Composable
fun TotalFocusTimeContent(
    focusTime: Duration,
    modifier: Modifier = Modifier,
) {
    val hasRecord = remember(focusTime) { focusTime.isZero.not() }
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
                AnimatedFocusTime(focusTime)

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

@Composable
fun AnimatedFocusTime(
    focusTime: Duration,
    modifier: Modifier = Modifier,
) {
    val targetHours = remember(focusTime) { focusTime.toHours().toInt() }
    val targetMinutes = remember(focusTime) { (focusTime.toMinutes() % 60).toInt() }
    var startAnimation by remember { mutableStateOf(false) }

    val animatedHours by animateIntAsState(
        targetValue = if (startAnimation) targetHours else 0,
        animationSpec = tween(
            durationMillis = targetHours * 100,
            easing = LinearEasing,
        ),
    )

    val animatedMinutes by animateIntAsState(
        targetValue = if (startAnimation) targetMinutes else 0,
        animationSpec = tween(
            durationMillis = targetMinutes * 100,
            delayMillis = 300,
            easing = LinearEasing,
        ),
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Text(
        text = stringResource(
            R.string.common_time_format,
            animatedHours,
            animatedMinutes,
        ),
        style = MnTheme.typography.header3,
        color = MnTheme.textColorScheme.inverse,
        modifier = modifier,
    )
}

private class TotalFocusTimeContentPreviewParameter :
    CollectionPreviewParameterProvider<Duration>(
        listOf(
            Duration.ofHours(10),
            Duration.ZERO,
        ),
    )

@ThemePreviews
@Composable
private fun TotalFocusTImeContentPreview(
    @PreviewParameter(TotalFocusTimeContentPreviewParameter::class) focusTime: Duration,
) {
    MnTheme {
        TotalFocusTimeContent(
            focusTime = focusTime,
        )
    }
}
