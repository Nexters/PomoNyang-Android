package com.pomonyang.presentation.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "LightTheme",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "DarkTheme",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class ThemePreviews

@Preview(
    name = "Normal",
    device = "spec:shape=Normal,width=1440,height=3200,unit=px,dpi=515",
    showSystemUi = true
)
@Preview(
    name = "Foldable",
    device = Devices.FOLDABLE,
    showSystemUi = true
)
annotation class DevicePreviews
