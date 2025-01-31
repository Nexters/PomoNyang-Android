package com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.component.Timer
import com.pomonyang.mohanyang.presentation.component.TimerSelectedButtons
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.DEFAULT_TIME
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.formatTime

@Composable
fun PomodoroBreakReadyRoute(
    goToHome: () -> Unit,
    forceGoHome: () -> Unit,
    goToPomodoroRest: () -> Unit,
    modifier: Modifier = Modifier,
    pomodoroBreakReadyViewModel: PomodoroBreakReadyViewModel = hiltViewModel(),
    onShowSnackbar: (String, Int?) -> Unit
) {
    val state by pomodoroBreakReadyViewModel.state.collectAsStateWithLifecycle()
    pomodoroBreakReadyViewModel.effects.collectWithLifecycle(minActiveState = Lifecycle.State.CREATED) { effect ->
        when (effect) {
            PomodoroBreakReadySideEffect.GoToPomodoroSetting -> goToHome()
            is PomodoroBreakReadySideEffect.ShowSnackbar -> onShowSnackbar(effect.message, effect.iconRes)
            PomodoroBreakReadySideEffect.GoToPomodoroRest -> goToPomodoroRest()
        }
    }

    BackHandler {
        // NOTHING
    }

    LaunchedEffect(state.forceGoHome) {
        if (state.forceGoHome) {
            forceGoHome()
        }
    }

    PomodoroBreakReadyScreen(
        type = state.type,
        focusedTime = state.focusedTime.formatTime(),
        exceededTime = state.exceededTime.formatTime(),
        plusButtonSelected = state.plusButtonSelected,
        minusButtonSelected = state.minusButtonSelected,
        plusButtonEnabled = state.plusButtonEnabled,
        minusButtonEnabled = state.minusButtonEnabled,
        onAction = { pomodoroBreakReadyViewModel.handleEvent(it) },
        modifier = modifier
    )
}

@Composable
private fun PomodoroBreakReadyScreen(
    type: String,
    focusedTime: String,
    exceededTime: String,
    plusButtonSelected: Boolean,
    minusButtonSelected: Boolean,
    plusButtonEnabled: Boolean,
    minusButtonEnabled: Boolean,
    onAction: (PomodoroBreakReadyEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        CategoryBox(categoryName = type, iconRes = PomodoroCategoryType.safeValueOf(type).iconRes)

        Timer(
            modifier = Modifier.padding(top = MnSpacing.small),
            time = focusedTime,
            exceededTime = exceededTime
        )

        RestLottie(isCompleteFocus = exceededTime != DEFAULT_TIME)

        TimerSelectedButtons(
            plusButtonSelected = plusButtonSelected,
            minusButtonSelected = minusButtonSelected,
            plusButtonEnabled = plusButtonEnabled,
            minusButtonEnabled = minusButtonEnabled,
            title = stringResource(R.string.change_focus_time_prompt),
            onPlusButtonClick = { onAction(PomodoroBreakReadyEvent.OnPlusButtonClick(plusButtonSelected.not())) },
            onMinusButtonClick = { onAction(PomodoroBreakReadyEvent.OnMinusButtonClick(minusButtonSelected.not())) }
        )
        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            modifier = Modifier.size(200.dp, 60.dp),
            styles = MnBoxButtonStyles.large,
            text = stringResource(id = R.string.rest_start),
            onClick = { onAction(PomodoroBreakReadyEvent.OnStartRestClick) },
            colors = MnBoxButtonColorType.primary
        )

        MnTextButton(
            styles = MnTextButtonStyles.large,
            containerPadding = PaddingValues(bottom = MnSpacing.xLarge),
            text = stringResource(id = R.string.focus_end),
            onClick = { onAction(PomodoroBreakReadyEvent.OnEndFocusClick) }
        )
    }
}

@Composable
private fun RestLottie(
    modifier: Modifier = Modifier,
    isCompleteFocus: Boolean
) {
    val restWaitingLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loti_rest_waiting))
    val completeFocusLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loti_rest_waiting_complete_focus))

    Box(
        modifier = modifier
            .padding(top = MnSpacing.xLarge)
            .size(240.dp)
    ) {
        LottieAnimation(
            composition = restWaitingLottie
        )

        if (isCompleteFocus) {
            LottieAnimation(composition = completeFocusLottie)
        }
    }
}

@DevicePreviews
@Composable
private fun PomodoroBreakReadyScreenPreview() {
    MnTheme {
        PomodoroBreakReadyScreen(
            type = "작업",
            focusedTime = "30:00",
            exceededTime = "05:30",
            plusButtonSelected = false,
            minusButtonSelected = true,
            plusButtonEnabled = true,
            minusButtonEnabled = true,
            onAction = {}
        )
    }
}
