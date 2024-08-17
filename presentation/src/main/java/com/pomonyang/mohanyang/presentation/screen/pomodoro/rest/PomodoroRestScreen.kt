package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.pomonyang.mohanyang.presentation.component.TimerSelectedButtons
import com.pomonyang.mohanyang.presentation.component.TimerType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.DEFAULT_TIME
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle

@Composable
fun PomodoroRestRoute(
    onShowSnackbar: suspend (String, String?) -> Unit,
    onBackPressed: () -> Unit,
    goToHome: () -> Unit,
    goToFocus: () -> Unit,
    modifier: Modifier = Modifier,
    pomodoroRestViewModel: PomodoroRestViewModel = hiltViewModel()
) {
    val state by pomodoroRestViewModel.state.collectAsStateWithLifecycle()
    var showTooltip by remember { mutableStateOf(true) }
    pomodoroRestViewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is PomodoroRestEffect.ShowSnackbar -> onShowSnackbar(effect.message, null)
            PomodoroRestEffect.GoToHome -> {
                showTooltip = false
                goToHome()
            }

            PomodoroRestEffect.GoToPomodoroFocus -> {
                showTooltip = false
                goToFocus()
            }
        }
    }

    BackHandler {
        showTooltip = false
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        pomodoroRestViewModel.handleEvent(PomodoroRestEvent.Init)
    }

    PomodoroRestScreen(
        modifier = modifier,
        type = state.type,
        time = state.displayRestTime(),
        showTooltip = showTooltip,
        plusButtonSelected = state.plusButtonSelected,
        minusButtonSelected = state.minusButtonSelected,
        plusButtonEnabled = state.plusButtonEnabled,
        minusButtonEnabled = state.minusButtonEnabled,
        exceededTime = state.displayExceedTime(),
        onAction = remember { pomodoroRestViewModel::handleEvent }
    )
}

@Composable
private fun PomodoroRestScreen(
    type: String,
    time: String,
    showTooltip: Boolean,
    exceededTime: String,
    plusButtonSelected: Boolean,
    minusButtonSelected: Boolean,
    plusButtonEnabled: Boolean,
    minusButtonEnabled: Boolean,
    onAction: (PomodoroRestEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val tooltipMessage = if (exceededTime != DEFAULT_TIME) R.string.rest_cat_tooltip else R.string.rest_exceed_cat_tooltip
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MnTopAppBar(
            navigationIcon = {
                CategoryBox(
                    categoryName = type,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CatRive(
            showTooltip = showTooltip,
            tooltipMessage = stringResource(id = tooltipMessage)
        )

        TimerType(type = stringResource(id = R.string.rest_time))

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
            type = "공부",
            time = "25:00",
            showTooltip = true,
            exceededTime = "00:00",
            plusButtonSelected = false,
            minusButtonSelected = true,
            plusButtonEnabled = true,
            minusButtonEnabled = true,
            onAction = {}
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroTimerExceedScreenPreview() {
    MnTheme {
        PomodoroRestScreen(
            type = "공부",
            time = "25:00",
            showTooltip = true,
            exceededTime = "10:00",
            plusButtonSelected = false,
            minusButtonSelected = true,
            plusButtonEnabled = true,
            minusButtonEnabled = true,
            onAction = {}
        )
    }
}