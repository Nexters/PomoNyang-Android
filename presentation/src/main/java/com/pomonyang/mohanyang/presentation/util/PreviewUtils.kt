package com.pomonyang.mohanyang.presentation.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "LightTheme",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xFFFAF6F3,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "DarkTheme",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xFFFAF6F3,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ThemePreviews

@Preview(
    name = "Normal",
    device = "spec:shape=Normal,width=1440,height=2800,unit=px,dpi=515",
    showBackground = true,
    backgroundColor = 0xFFFAF6F3,
)
@Preview(
    name = "Short",
    device = "spec:shape=Normal,width=1440,height=2000,unit=px,dpi=515",
    showBackground = true,
    backgroundColor = 0xFFFAF6F3,
)
@Preview(
    name = "Foldable",
    device = Devices.FOLDABLE,
    showBackground = true,
    backgroundColor = 0xFFFAF6F3,
)
annotation class DevicePreviews
