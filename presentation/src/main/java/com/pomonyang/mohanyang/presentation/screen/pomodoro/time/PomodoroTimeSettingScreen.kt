package com.pomonyang.mohanyang.presentation.screen.pomodoro.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.picker.MnWheelMinutePicker
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.SettingButton
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.collections.immutable.toPersistentList
import timber.log.Timber

@Composable
fun PomodoroTimeSettingRoute(
    isFocusTime: Boolean,
    initialSettingTime: Int,
    modifier: Modifier = Modifier,
    viewModel: PomodoroTimeSettingViewModel = hiltViewModel(),
    onShowSnackbar: (String, String?) -> Unit,
    onEndSettingClick: () -> Unit
) {
    viewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is PomodoroTimeSettingEffect.GoToPomodoroSettingScreen -> {
                val message = if (isFocusTime) "집중" else "휴식"
                onShowSnackbar("${message}시간을 변경했어요", null)
                onEndSettingClick()
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.handleEvent(
            PomodoroTimeSettingEvent.Init(isFocusTime = isFocusTime)
        )
    }

    PomodoroTimeSettingScreen(
        modifier = modifier,
        isFocusTime = isFocusTime,
        initialSettingTime = initialSettingTime,
        onAction = viewModel::handleEvent
    )
}

@Composable
private fun PomodoroTimeSettingScreen(
    initialSettingTime: Int,
    isFocusTime: Boolean,
    onAction: (PomodoroTimeSettingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconResId = if (isFocusTime) R.drawable.ic_focus else R.drawable.ic_rest

    val text = if (isFocusTime) R.string.focus else R.string.rest
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TimeSettingTopAppBar { /* TODO */ }

        CategoryBox(
            iconRes = iconResId,
            categoryName = stringResource(id = text),
            backgroundColor = Color.Transparent
        )

        Timber.tag("koni").d("PomodoroTimeSettingScreen > $initialSettingTime")
        MnWheelMinutePicker(
            modifier = Modifier.padding(top = 16.dp),
            items = (10..60 step 5).toPersistentList(),
            initialItem = initialSettingTime,
            onChangePickTime = remember { { onAction(PomodoroTimeSettingEvent.ChangePickTime(time = it)) } },
            selectedIconResId = iconResId
        )

        SettingButton(
            modifier = Modifier.padding(bottom = 40.dp),
            backgroundColor = MnTheme.backgroundColorScheme.inverse,
            onClick = {
                onAction(PomodoroTimeSettingEvent.Submit)
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimeSettingTopAppBar(
    onActionClick: () -> Unit
) {
    MnTopAppBar(
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable { onActionClick() },
                contentAlignment = Alignment.Center
            ) {
                MnMediumIcon(
                    resourceId = R.drawable.ic_close,
                    tint = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PomodoroTimeSettingScreenPreview() {
    MnTheme {
        PomodoroTimeSettingScreen(
            initialSettingTime = 25,
            isFocusTime = true,
            onAction = {}
        )
    }
}
