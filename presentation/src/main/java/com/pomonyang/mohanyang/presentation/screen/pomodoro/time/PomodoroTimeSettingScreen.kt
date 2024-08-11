package com.pomonyang.mohanyang.presentation.screen.pomodoro.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.picker.MnWheelMinutePicker
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.SettingButton
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PomodoroTimeSettingRoute(
    initialSettingTime: Int,
    isFocusTime: Boolean,
    modifier: Modifier = Modifier,
    onEndSettingClick: (time: Int) -> Unit
) {
    PomodoroTimeSettingScreen(
        modifier = modifier,
        isFocusTime = isFocusTime,
        initialSettingTime = initialSettingTime,
        onEndSettingClick = {
            onEndSettingClick(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PomodoroTimeSettingScreen(
    initialSettingTime: Int,
    isFocusTime: Boolean,
    onEndSettingClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPickTime: Int = initialSettingTime
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MnTopAppBar(
            actions = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .noRippleClickable { },
                    contentAlignment = Alignment.Center
                ) {
                    MnMediumIcon(
                        resourceId = R.drawable.ic_null,
                        tint = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
                    )
                }
            }
        )
        Row(
            modifier = Modifier.padding(
                vertical = MnSpacing.small,
                horizontal = MnSpacing.large
            )
        ) {
            val resId = if (isFocusTime) R.drawable.ic_null else R.drawable.ic_null
            val text = if (isFocusTime) R.string.focus else R.string.rest
            MnMediumIcon(
                resourceId = resId
            )
            Text(
                modifier = Modifier.padding(start = MnSpacing.xSmall),
                text = stringResource(id = text)
            )
        }
        MnWheelMinutePicker(
            modifier = Modifier.weight(1f),
            items = (5..60 step 5).toPersistentList(),
            initialItem = initialSettingTime,
            halfDisplayCount = 3,
            onChangePickTime = { currentPickTime = it }
        )

        SettingButton(
            modifier = Modifier.padding(bottom = 55.dp),
            backgroundColor = MnTheme.backgroundColorScheme.inverse,
            onClick = {
                onEndSettingClick(currentPickTime)
            }
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
            onEndSettingClick = {}
        )
    }
}
