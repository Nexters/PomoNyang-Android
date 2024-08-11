package com.pomonyang.mohanyang.presentation.screen.pomodoro.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.picker.MnWheelMinutePicker
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.SettingButton
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PomodoroTimeSettingRoute(
    initialFocusTime: Int,
    isFocusTime: Boolean,
    initialRestTime: Int,
    categoryNo: Int,
    type: String,
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
            PomodoroTimeSettingEvent.Init(
                isFocusTime = isFocusTime,
                categoryNo = categoryNo,
                titleName = type,
                focusTime = initialFocusTime,
                restTime = initialRestTime
            )
        )
    }

    PomodoroTimeSettingScreen(
        modifier = modifier,
        isFocusTime = isFocusTime,
        initialSettingTime = if (isFocusTime) initialFocusTime else initialRestTime,
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
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeSettingTopAppBar { /* TODO */ }

        CategoryBox(isFocusTime = isFocusTime)

        Spacer(modifier = Modifier.weight(1f))

        MnWheelMinutePicker(
            items = (5..60 step 5).toPersistentList(),
            initialItem = initialSettingTime,
            onChangePickTime = { onAction(PomodoroTimeSettingEvent.ChangePickTime(time = it)) }
        )

        Spacer(modifier = Modifier.weight(1f))

        SettingButton(
            modifier = Modifier.padding(bottom = 55.dp),
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
                    resourceId = R.drawable.ic_null,
                    tint = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
                )
            }
        }
    )
}

@Composable
private fun CategoryBox(
    isFocusTime: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(
            vertical = MnSpacing.small,
            horizontal = MnSpacing.large
        )
    ) {
        val iconResId = if (isFocusTime) R.drawable.ic_null else R.drawable.ic_null
        val text = if (isFocusTime) R.string.focus else R.string.rest
        MnMediumIcon(
            resourceId = iconResId
        )
        Text(
            modifier = Modifier.padding(start = MnSpacing.small),
            text = stringResource(id = text)
        )
    }
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
