package com.pomonyang.mohanyang.presentation.screen.pomodoro.focus

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.component.Timer
import com.pomonyang.mohanyang.presentation.component.TimerType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.DEFAULT_TIME
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroTimerEffect
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroTimerEvent
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroTimerViewModel
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.notifyFocusEnd
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.startInterrupt
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.stopInterrupt
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.startTimer
import com.pomonyang.mohanyang.presentation.util.stopTimer

@Composable
fun PomodoroFocusRoute(
    pomodoroTimerViewModel: PomodoroTimerViewModel,
    modifier: Modifier = Modifier,
    pomodoroFocusViewModel: PomodoroFocusViewModel = hiltViewModel(),
    goToRest: (type: String, focusTime: Int, exceededTime: Int) -> Unit,
    goToHome: () -> Unit
) {
    val state by pomodoroTimerViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler {
        // NOTHING
    }

    pomodoroFocusViewModel.effects.collectWithLifecycle(minActiveState = Lifecycle.State.CREATED) { effect ->
        when (effect) {
            is PomodoroFocusEffect.GoToPomodoroRest -> {
                goToRest(
                    context.getString(state.categoryType.kor),
                    state.focusTime,
                    state.focusExceededTime
                )
            }

            PomodoroFocusEffect.GoToPomodoroSetting -> {
                goToHome()
            }

            PomodoroFocusEffect.StartFocusAlarm -> {
                startInterrupt(context)
            }

            PomodoroFocusEffect.StopFocusAlarm -> {
                stopInterrupt(context)
            }
        }
    }

    pomodoroTimerViewModel.effects.collectWithLifecycle(minActiveState = Lifecycle.State.CREATED) { effect ->
        if (effect is PomodoroTimerEffect.PomodoroFocusEffect) {
            when (effect) {
                PomodoroTimerEffect.PomodoroFocusEffect.ForceGoRest -> {
                    goToRest(
                        context.getString(state.categoryType.kor),
                        state.focusTime,
                        state.focusExceededTime
                    )
                }

                PomodoroTimerEffect.PomodoroFocusEffect.SendEndFocusAlarm -> notifyFocusEnd(context)
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        pomodoroFocusViewModel.handleEvent(PomodoroFocusEvent.Resume)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        pomodoroFocusViewModel.handleEvent(PomodoroFocusEvent.Pause)
    }

    DisposableEffect(key1 = Unit) {
        pomodoroTimerViewModel.handleEvent(PomodoroTimerEvent.Start)
        context.startTimer(true)
        onDispose {
            stopInterrupt(context)
            context.stopTimer(true)
        }
    }

    PomodoroTimerScreen(
        modifier = modifier,
        title = state.title,
        categoryType = state.categoryType,
        catType = state.cat,
        time = state.displayFocusTime(),
        exceededTime = state.displayFocusExceedTime(),
        onAction = remember { pomodoroFocusViewModel::handleEvent }
    )
}

@Composable
private fun PomodoroTimerScreen(
    title: String,
    categoryType: PomodoroCategoryType,
    catType: CatType,
    time: String,
    exceededTime: String,
    onAction: (PomodoroFocusEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val tooltipMessage = if (exceededTime != DEFAULT_TIME) R.string.exceed_cat_tooltip else R.string.focus_cat_tooltip
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MnTopAppBar(
            navigationIcon = {
                CategoryBox(
                    iconRes = categoryType.iconRes,
                    categoryName = title,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CatRive(
            tooltipMessage = stringResource(id = tooltipMessage),
            riveResource = R.raw.cat_focus,
            stateMachineInput = catType.pomodoroRiveCat,
            stateMachineName = "State Machine_Focus",
            isAutoPlay = false,
            onRiveClick = {
                it.fireState("State Machine_Focus", catType.catFireInput)
            }
        )

        TimerType(type = stringResource(id = R.string.focus_time), iconRes = R.drawable.ic_focus)

        Timer(
            modifier = Modifier.weight(1f),
            time = time,
            exceededTime = exceededTime
        )

        MnBoxButton(
            modifier = Modifier.size(200.dp, 60.dp),
            styles = MnBoxButtonStyles.large,
            text = stringResource(id = R.string.rest_action),
            onClick = { onAction(PomodoroFocusEvent.ClickRest) },
            colors = if (exceededTime != DEFAULT_TIME) MnBoxButtonColorType.primary else MnBoxButtonColorType.secondary
        )

        MnTextButton(
            styles = MnTextButtonStyles.large,
            containerPadding = PaddingValues(bottom = MnSpacing.xLarge),
            text = stringResource(id = R.string.focus_end),
            onClick = { onAction(PomodoroFocusEvent.ClickHome) }
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerScreenPreview() {
    MnTheme {
        PomodoroTimerScreen(
            title = "공부",
            categoryType = PomodoroCategoryType.DEFAULT,
            time = "25:00",
            exceededTime = "00:00",
            onAction = {},
            catType = CatType.CHEESE
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerExceedScreenPreview() {
    MnTheme {
        PomodoroTimerScreen(
            title = "공부",
            categoryType = PomodoroCategoryType.DEFAULT,
            time = "25:00",
            exceededTime = "10:00",
            onAction = {},
            catType = CatType.CHEESE
        )
    }
}