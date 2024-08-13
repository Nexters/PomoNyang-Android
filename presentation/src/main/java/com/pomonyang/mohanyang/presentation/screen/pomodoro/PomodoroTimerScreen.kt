package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.pomodoro.component.CategoryBox
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews

@Composable
fun PomodoroTimerRoute(
    modifier: Modifier = Modifier
) {
    PomodoroTimerScreen(
        modifier = modifier,
        isFocus = true,
        time = "25:00",
        exceededTime = "00:05"
    )
}

@Composable
private fun PomodoroTimerScreen(
    isFocus: Boolean,
    time: String,
    exceededTime: String,
    modifier: Modifier = Modifier
) {
    val typeRes = if (isFocus) R.string.focus_action else R.string.rest_action
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MnTopAppBar(
            navigationIcon = {
                CategoryBox(categoryName = "작업")
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CatRive()

        Timer(
            title = stringResource(id = typeRes),
            time = time,
            exceededTime = exceededTime,
            modifier = Modifier.padding(top = MnSpacing.xLarge)
        )

        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            modifier = Modifier.size(200.dp, 60.dp),
            styles = MnBoxButtonStyles.large,
            text = "휴식하기",
            onClick = { },
            colors = if (exceededTime.isNotEmpty()) MnBoxButtonColorType.primary else MnBoxButtonColorType.secondary
        )

        MnTextButton(
            modifier = Modifier.padding(bottom = MnSpacing.xLarge),
            styles = MnTextButtonStyles.large,
            text = "집중 끝내기",
            onClick = {}
        )
    }
}

@Composable
private fun CatRive(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(240.dp)
            .background(MnTheme.backgroundColorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "image")
    }
}

@Composable
fun Timer(
    title: String,
    time: String,
    exceededTime: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            MnSmallIcon(resourceId = R.drawable.ic_null)
            Text(
                modifier = Modifier.padding(MnSpacing.xSmall),
                text = "휴식하기",
                style = MnTheme.typography.header5,
                color = MnTheme.textColorScheme.secondary
            )
        }

        Text(
            text = time,
            style = MnTheme.typography.header1,
            color = MnTheme.textColorScheme.primary
        )
        if (exceededTime.isNotEmpty()) {
            Text(
                text = "$exceededTime 초과",
                style = MnTheme.typography.header4,
                color = MnTheme.backgroundColorScheme.accent1
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerScreenPreview() {
    MnTheme {
        PomodoroTimerRoute()
    }
}
