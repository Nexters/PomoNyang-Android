package com.pomonyang.mohanyang.presentation.designsystem.token

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Immutable
object MnColor {
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)

    val Gray50 = Color(0xFFFAF6F3)
    val Gray100 = Color(0xFFF3EEEB)
    val Gray200 = Color(0xFFDFD8D2)
    val Gray300 = Color(0xFFB8AFA8)
    val Gray400 = Color(0xFFA39A93)
    val Gray500 = Color(0xFF8F867E)
    val Gray600 = Color(0xFF665E57)
    val Gray700 = Color(0xFF524A44)
    val Gray800 = Color(0xFF3D3732)
    val Gray900 = Color(0xFF292621)

    val Orange50 = Color(0xFFFFF2E6)
    val Orange100 = Color(0xFFFFDBBA)
    val Orange200 = Color(0xFFFFC48E)
    val Orange300 = Color(0xFFFFAD62)
    val Orange400 = Color(0xFFFF9636)
    val Orange500 = Color(0xFFF47A0A)
    val Orange600 = Color(0xFFCB6100)
    val Orange700 = Color(0xFFA24E00)
    val Orange800 = Color(0xFF7A3A00)
    val Orange900 = Color(0xFF512700)

    val Red50 = Color(0xFFFFEBE7)
    val Red100 = Color(0xFFFFC7BC)
    val Red200 = Color(0xFFFFA290)
    val Red300 = Color(0xFFFF7E65)
    val Red400 = Color(0xFFFF5A3A)
    val Red500 = Color(0xFFFE360F)
    val Red600 = Color(0xFFD52300)
    val Red700 = Color(0xFFAC1C00)
    val Red800 = Color(0xFF841600)
    val Red900 = Color(0xFF5B0F00)
}

@Immutable
class MnColorScheme(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val inverse: Color,
    val disabled: Color = Color.Unspecified,
    val accent1: Color = Color.Unspecified,
    val accent2: Color = Color.Unspecified,
)

private val MnIconColorScheme = MnColorScheme(
    primary = MnColor.Gray700,
    secondary = MnColor.Gray500,
    tertiary = MnColor.Gray300,
    disabled = MnColor.Gray200,
    inverse = MnColor.White,
)

private val MnTextColorScheme = MnColorScheme(
    primary = MnColor.Gray800,
    secondary = MnColor.Gray600,
    tertiary = MnColor.Gray500,
    disabled = MnColor.Gray300,
    inverse = MnColor.White,
)

private val MnBackgroundColorScheme = MnColorScheme(
    primary = MnColor.Gray50,
    secondary = MnColor.Gray100,
    tertiary = MnColor.Gray400,
    inverse = MnColor.Gray900,
    accent1 = MnColor.Orange500,
    accent2 = MnColor.Orange50,
)

val LocalMnIconColorScheme = staticCompositionLocalOf { MnIconColorScheme }
val LocalMnTextColorScheme = staticCompositionLocalOf { MnTextColorScheme }
val LocalMnBackgroundColorScheme = staticCompositionLocalOf { MnBackgroundColorScheme }

@ThemePreviews
@Composable
private fun MnColorSchemePreview() {
    MnTheme {
        Column(modifier = Modifier.background(MnTheme.backgroundColorScheme.accent1)) {
            Text(text = "test", color = MnTheme.textColorScheme.inverse)
        }
    }
}
