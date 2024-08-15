package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.component.Timer
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle

@Composable
fun PomodoroTimerRoute(
    modifier: Modifier = Modifier,
    pomodoroTimerViewModel: PomodoroTimerViewModel = hiltViewModel(),
    goToRest: () -> Unit,
    goToPomodoroSetting: () -> Unit
) {
    val state by pomodoroTimerViewModel.state.collectAsStateWithLifecycle()

    pomodoroTimerViewModel.effects.collectWithLifecycle {
        when (it) {
            PomodoroTimerEffect.GoToPomodoroRest -> goToRest()
            PomodoroTimerEffect.GoToPomodoroSetting -> goToPomodoroSetting()
        }
    }

    LaunchedEffect(Unit) {
        pomodoroTimerViewModel.handleEvent(PomodoroTimerEvent.Init)
    }

    PomodoroTimerScreen(
        modifier = modifier,
        type = state.type,
        isFocus = true,
        time = state.displayFocusTime(),
        exceededTime = state.displayRestTime(),
        onAction = remember { pomodoroTimerViewModel::handleEvent }
    )
}

@Composable
private fun PomodoroTimerScreen(
    isFocus: Boolean,
    type: String,
    time: String,
    exceededTime: String,
    onAction: (PomodoroTimerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val typeRes = if (isFocus) R.string.focus_time else R.string.rest_time
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MnTopAppBar(
            navigationIcon = {
                CategoryBox(categoryName = type, modifier = Modifier.padding(start = 12.dp))
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CatRive()

        Row(
            modifier = Modifier.padding(top = MnSpacing.xLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MnSmallIcon(resourceId = R.drawable.ic_null)
            Text(
                modifier = Modifier.padding(MnSpacing.xSmall),
                text = stringResource(id = typeRes),
                style = MnTheme.typography.header5,
                color = MnTheme.textColorScheme.secondary
            )
        }

        Timer(
            time = time,
            exceededTime = exceededTime
        )

        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            modifier = Modifier.size(200.dp, 60.dp),
            styles = MnBoxButtonStyles.large,
            text = stringResource(id = R.string.rest_action),
            onClick = { onAction(PomodoroTimerEvent.ClickRest) },
            colors = if (exceededTime.isNotEmpty()) MnBoxButtonColorType.primary else MnBoxButtonColorType.secondary
        )

        MnTextButton(
            styles = MnTextButtonStyles.large,
            containerPadding = PaddingValues(bottom = MnSpacing.xLarge),
            text = stringResource(id = R.string.focus_end),
            onClick = { onAction(PomodoroTimerEvent.ClickHome) }
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

@DevicePreviews
@Composable
private fun PomodoroTimerScreenPreview() {
    MnTheme {
        PomodoroTimerScreen(
            type = "공부",
            time = "25:00",
            exceededTime = "00:00",
            isFocus = true,
            onAction = {}
        )
    }
}
