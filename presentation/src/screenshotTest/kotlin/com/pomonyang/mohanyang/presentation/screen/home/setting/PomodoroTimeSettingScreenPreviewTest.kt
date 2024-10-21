package com.pomonyang.mohanyang.presentation.screen.home.setting

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.home.time.PomodoroTimeSettingScreen
import com.pomonyang.mohanyang.presentation.theme.MnTheme

class PomodoroTimeSettingScreenPreviewTest {

    @Preview(showBackground = true)
    @Composable
    private fun PomodoroTimeSettingScreenPreview() {
        MnTheme {
            PomodoroTimeSettingScreen(
                initialSettingTime = 25,
                isFocusTime = true,
                onAction = {},
                category = PomodoroCategoryType.DEFAULT
            )
        }
    }
}
