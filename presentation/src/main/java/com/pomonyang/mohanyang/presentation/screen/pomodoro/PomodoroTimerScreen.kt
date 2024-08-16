package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
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
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.DEFAULT_TIME
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle

@Composable
fun PomodoroTimerRoute(
    modifier: Modifier = Modifier,
    pomodoroTimerViewModel: PomodoroTimerViewModel = hiltViewModel(),
    goToRest: (type: String, focusTime: Int, exceededTime: Int) -> Unit,
    goToPomodoroSetting: () -> Unit,
    onBackPressed: () -> Unit
) {
    val state by pomodoroTimerViewModel.state.collectAsStateWithLifecycle()
    var showTooltip by remember { mutableStateOf(true) }

    pomodoroTimerViewModel.effects.collectWithLifecycle {
        when (it) {
            is PomodoroTimerEffect.GoToPomodoroRest -> {
                showTooltip = false
                goToRest(it.title, it.focusTime, it.exceededTime)
            }

            PomodoroTimerEffect.GoToPomodoroSetting -> {
                showTooltip = false
                goToPomodoroSetting()
            }
        }
    }

    BackHandler {
        showTooltip = false
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        pomodoroTimerViewModel.handleEvent(PomodoroTimerEvent.Init)
    }

    PomodoroTimerScreen(
        modifier = modifier,
        title = state.title,
        type = state.type,
        time = state.displayFocusTime(),
        showTooltip = showTooltip,
        exceededTime = state.displayExceedTime(),
        onAction = remember { pomodoroTimerViewModel::handleEvent }
    )
}

@Composable
private fun PomodoroTimerScreen(
    title: String,
    type: PomodoroCategoryType,
    time: String,
    showTooltip: Boolean,
    exceededTime: String,
    onAction: (PomodoroTimerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val tooltipMessage = if (exceededTime != DEFAULT_TIME) R.string.exceed_cat_tooltip else R.string.focus_cat_tooltip
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MnTopAppBar(
            navigationIcon = {
                CategoryBox(
                    iconRes = type.iconRes,
                    categoryName = title,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CatRive(
            showTooltip = showTooltip,
            tooltipMessage = stringResource(id = tooltipMessage)
        )

        Row(
            modifier = Modifier.padding(top = MnSpacing.xLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MnSmallIcon(resourceId = R.drawable.ic_null)
            Text(
                modifier = Modifier.padding(MnSpacing.xSmall),
                text = stringResource(id = R.string.focus_time),
                style = MnTheme.typography.header5,
                color = MnTheme.textColorScheme.secondary
            )
        }

        Timer(
            modifier = Modifier.weight(1f),
            time = time,
            exceededTime = exceededTime
        )

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

@DevicePreviews
@Composable
private fun PomodoroTimerScreenPreview() {
    MnTheme {
        PomodoroTimerScreen(
            title = "공부",
            type = PomodoroCategoryType.DEFAULT,
            time = "25:00",
            showTooltip = true,
            exceededTime = "00:00",
            onAction = {}
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerExceedScreenPreview() {
    MnTheme {
        PomodoroTimerScreen(
            title = "공부",
            type = PomodoroCategoryType.DEFAULT,
            time = "25:00",
            showTooltip = true,
            exceededTime = "10:00",
            onAction = {}
        )
    }
}
