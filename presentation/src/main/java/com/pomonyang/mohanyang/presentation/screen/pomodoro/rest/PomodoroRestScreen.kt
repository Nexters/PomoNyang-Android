package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.component.Timer
import com.pomonyang.mohanyang.presentation.component.TimerSelectedButtons
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
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroTimerViewModel
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.startTimer
import com.pomonyang.mohanyang.presentation.util.stopTimer

@Composable
fun PomodoroRestRoute(
    pomodoroTimerViewModel: PomodoroTimerViewModel,
    onShowSnackbar: suspend (String, Int?) -> Unit,
    goToHome: () -> Unit,
    goToFocus: () -> Unit,
    modifier: Modifier = Modifier,
    pomodoroRestViewModel: PomodoroRestViewModel = hiltViewModel()
) {
    val timerState by pomodoroTimerViewModel.state.collectAsStateWithLifecycle()
    val restState by pomodoroRestViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    pomodoroRestViewModel.effects.collectWithLifecycle(minActiveState = Lifecycle.State.CREATED) { effect ->
        when (effect) {
            is PomodoroRestEffect.ShowSnackbar -> onShowSnackbar(effect.message, effect.iconRes)
            PomodoroRestEffect.GoToHome -> {
                goToHome()
            }

            PomodoroRestEffect.GoToPomodoroFocus -> {
                goToFocus()
            }
        }
    }

    BackHandler {
        // NOTHING
    }

    LaunchedEffect(timerState.maxRestTime) {
        if (timerState.maxRestTime != 0) {
            context.startTimer(false, timerState.maxRestTime)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            context.stopTimer(false)
        }
    }

    PomodoroRestScreen(
        modifier = modifier,
        categoryType = stringResource(id = timerState.categoryType.kor),
        catType = timerState.cat,
        time = timerState.displayRestTime(),
        plusButtonSelected = restState.plusButtonSelected,
        minusButtonSelected = restState.minusButtonSelected,
        plusButtonEnabled = restState.plusButtonEnabled,
        minusButtonEnabled = restState.minusButtonEnabled,
        exceededTime = timerState.displayRestExceedTime(),
        onAction = remember { pomodoroRestViewModel::handleEvent }
    )
}

@Composable
private fun PomodoroRestScreen(
    categoryType: String,
    time: String,
    exceededTime: String,
    catType: CatType,
    plusButtonSelected: Boolean,
    minusButtonSelected: Boolean,
    plusButtonEnabled: Boolean,
    minusButtonEnabled: Boolean,
    onAction: (PomodoroRestEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val tooltipMessage = if (exceededTime != DEFAULT_TIME) R.string.rest_exceed_cat_tooltip else R.string.rest_cat_tooltip
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
                    categoryName = categoryType,
                    modifier = Modifier.padding(start = 12.dp),
                    iconRes = PomodoroCategoryType.safeValueOf(categoryType).iconRes
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CatRive(
            tooltipMessage = stringResource(id = tooltipMessage),
            riveResource = R.raw.cat_rest,
            stateMachineName = "State Machine_Home",
            stateMachineInput = catType.pomodoroRiveCat,
            isAutoPlay = false,
            onRiveClick = {
                it.fireState("State Machine_Home", catType.catFireInput)
            }
        )

        TimerType(type = stringResource(id = R.string.rest_time), iconRes = R.drawable.ic_rest)

        Timer(
            modifier = Modifier,
            time = time,
            exceededTime = exceededTime
        )

        TimerSelectedButtons(
            plusButtonSelected = plusButtonSelected,
            minusButtonSelected = minusButtonSelected,
            plusButtonEnabled = plusButtonEnabled,
            minusButtonEnabled = minusButtonEnabled,
            title = stringResource(R.string.change_rest_time_prompt),
            onPlusButtonClick = { onAction(PomodoroRestEvent.OnPlusButtonClick(plusButtonSelected.not())) },
            onMinusButtonClick = { onAction(PomodoroRestEvent.OnMinusButtonClick(minusButtonSelected.not())) }
        )

        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            modifier = Modifier.size(200.dp, 60.dp),
            styles = MnBoxButtonStyles.large,
            text = stringResource(id = R.string.one_more_focus),
            onClick = { onAction(PomodoroRestEvent.OnFocusClick) },
            colors = if (exceededTime != DEFAULT_TIME) MnBoxButtonColorType.primary else MnBoxButtonColorType.secondary
        )

        MnTextButton(
            styles = MnTextButtonStyles.large,
            containerPadding = PaddingValues(bottom = MnSpacing.xLarge),
            text = stringResource(id = R.string.focus_end),
            onClick = { onAction(PomodoroRestEvent.OnEndPomodoroClick) }
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerScreenPreview() {
    MnTheme {
        PomodoroRestScreen(
            categoryType = "공부",
            time = "25:00",
            exceededTime = "00:00",
            plusButtonSelected = false,
            minusButtonSelected = true,
            plusButtonEnabled = true,
            minusButtonEnabled = true,
            catType = CatType.CHEESE,
            onAction = {}
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerExceedScreenPreview() {
    MnTheme {
        PomodoroRestScreen(
            categoryType = "공부",
            time = "25:00",
            exceededTime = "10:00",
            plusButtonSelected = false,
            minusButtonSelected = true,
            plusButtonEnabled = true,
            minusButtonEnabled = true,
            catType = CatType.CHEESE,
            onAction = {}
        )
    }
}
