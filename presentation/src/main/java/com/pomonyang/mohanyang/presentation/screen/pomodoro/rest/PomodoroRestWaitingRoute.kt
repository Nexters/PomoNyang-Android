package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

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
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.formatTime
import timber.log.Timber

@Composable
fun PomodoroRestWaitingRoute(
    type: String,
    focusTime: Int,
    exceedTime: Int,
    goToPomodoroSetting: () -> Unit,
    modifier: Modifier = Modifier,
    pomodoroRestViewModel: PomodoroRestViewModel = hiltViewModel(),
    onShowSnackbar: (String, String?) -> Unit
) {
    val state by pomodoroRestViewModel.state.collectAsStateWithLifecycle()
    pomodoroRestViewModel.effects.collectWithLifecycle { effect ->
        Timber.tag("koni").d("effect > $effect")
        when (effect) {
            PomodoroRestSideEffect.GoToPomodoroSetting -> goToPomodoroSetting()
            is PomodoroRestSideEffect.ShowSnackbar -> onShowSnackbar(effect.message, null)
            PomodoroRestSideEffect.GoToPomodoroRest -> {
            }
        }
    }

    LaunchedEffect(Unit) {
        pomodoroRestViewModel.handleEvent(
            PomodoroRestWaitingEvent.Init(
                type = type,
                exceedTime = exceedTime,
                focusTime = focusTime
            )
        )
    }

    PomodoroRestWaitingScreen(
        type = type,
        focusTime = focusTime.formatTime(),
        exceedTime = exceedTime.formatTime(),
        plusButtonSelected = state.plusButtonSelected,
        minusButtonSelected = state.minusButtonSelected,
        plusButtonEnabled = state.plusButtonEnabled,
        minusButtonEnabled = state.minusButtonEnabled,
        onAction = { pomodoroRestViewModel.handleEvent(it) },
        modifier = modifier
    )
}

@Composable
private fun PomodoroRestWaitingScreen(
    type: String,
    focusTime: String,
    exceedTime: String,
    plusButtonSelected: Boolean,
    minusButtonSelected: Boolean,
    plusButtonEnabled: Boolean,
    minusButtonEnabled: Boolean,
    onAction: (PomodoroRestWaitingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = { onAction(PomodoroRestWaitingEvent.OnNavigationClick) },
                    iconResourceId = R.drawable.ic_null
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        CategoryBox(categoryName = type)

        Timer(
            modifier = Modifier.padding(top = MnSpacing.small),
            time = focusTime,
            exceededTime = exceedTime
        )

        CatRive(
            modifier = Modifier.padding(top = MnSpacing.xLarge)
        )

        FocusTimerSelectedButtons(
            onAction = onAction,
            plusButtonSelected = plusButtonSelected,
            minusButtonSelected = minusButtonSelected,
            plusButtonEnabled = plusButtonEnabled,
            minusButtonEnabled = minusButtonEnabled
        )

        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            modifier = Modifier.size(200.dp, 60.dp),
            styles = MnBoxButtonStyles.large,
            text = stringResource(id = R.string.rest_start),
            onClick = { onAction(PomodoroRestWaitingEvent.OnStartRestClick) },
            colors = MnBoxButtonColorType.primary
        )

        MnTextButton(
            styles = MnTextButtonStyles.large,
            containerPadding = PaddingValues(bottom = MnSpacing.xLarge),
            text = stringResource(id = R.string.focus_end),
            onClick = { onAction(PomodoroRestWaitingEvent.OnEndFocusClick) }
        )
    }
}

@Composable
private fun FocusTimerSelectedButtons(
    modifier: Modifier = Modifier,
    onAction: (PomodoroRestWaitingEvent) -> Unit,
    plusButtonSelected: Boolean,
    minusButtonSelected: Boolean,
    plusButtonEnabled: Boolean,
    minusButtonEnabled: Boolean
) {
    Text(
        modifier = Modifier.padding(top = MnSpacing.xLarge),
        text = stringResource(R.string.change_focus_time_prompt),
        style = MnTheme.typography.bodySemiBold,
        color = MnTheme.textColorScheme.disabled
    )
    Row(
        modifier = modifier.padding(top = MnSpacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        MnSelectButton(
            modifier = Modifier.padding(
                vertical = MnSpacing.small,
                horizontal = MnSpacing.medium
            ),
            isEnabled = plusButtonEnabled,
            isSelected = plusButtonSelected,
            onClick = { onAction(PomodoroRestWaitingEvent.OnPlusButtonClick(plusButtonSelected.not())) },
            leftIconResourceId = R.drawable.ic_null,
            subTitleContent = {
                Text(text = stringResource(R.string.five_minutes))
            }
        )
        MnSelectButton(
            modifier = Modifier.padding(
                vertical = MnSpacing.small,
                horizontal = MnSpacing.medium
            ),
            isEnabled = minusButtonEnabled,
            isSelected = minusButtonSelected,
            onClick = { onAction(PomodoroRestWaitingEvent.OnMinusButtonClick(minusButtonSelected.not())) },
            leftIconResourceId = R.drawable.ic_null,
            subTitleContent = {
                Text(text = stringResource(R.string.five_minutes))
            }
        )
    }
}

@DevicePreviews
@Composable
private fun PomodoroRestScreenPreview() {
    MnTheme {
        PomodoroRestWaitingScreen(
            type = "작업",
            focusTime = "30:00",
            exceedTime = "05:30",
            plusButtonSelected = false,
            minusButtonSelected = true,
            plusButtonEnabled = true,
            minusButtonEnabled = true,
            onAction = {}
        )
    }
}
