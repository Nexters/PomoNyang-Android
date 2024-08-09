package com.pomonyang.mohanyang.presentation.designsystem.token

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

private val pretendardStyle = FontFamily(
    Font(R.font.pretendard_regular, weight = FontWeight.Normal),
    Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
    Font(R.font.pretendard_bold, weight = FontWeight.Bold)
)

private fun setDefaultLineHeight(
    alignment: LineHeightStyle.Alignment = LineHeightStyle.Alignment.Center,
    trim: LineHeightStyle.Trim = LineHeightStyle.Trim.None
) = LineHeightStyle(
    alignment = alignment,
    trim = trim
)

@Immutable
data class MnTypography(
    val time: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 77.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.02).em

    ),
    val header1: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 58.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.02).em
    ),
    val header2: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.02).em
    ),
    val header3: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 38.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.02).em
    ),
    val header4: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.02).em
    ),
    val header5: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.02).em
    ),
    val bodySemiBold: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.01).em
    ),
    val bodyRegular: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.01).em
    ),

    val subBodySemiBold: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.01).em
    ),

    val subBodyRegular: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.01).em
    ),
    val captionSemiBold: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.01).em
    ),

    val captionRegular: TextStyle = TextStyle(
        fontFamily = pretendardStyle,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        lineHeightStyle = setDefaultLineHeight(),
        letterSpacing = (-0.01).em
    )
)

internal val LocalMnTypo = staticCompositionLocalOf { MnTypography() }

@ThemePreviews
@DevicePreviews
@Composable
fun TypoPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    ) {
        Text("time", style = MnTheme.typography.time)

        Text("header1", style = MnTheme.typography.header1)
        Text("header2", style = MnTheme.typography.header2)
        Text("header3", style = MnTheme.typography.header3)
        Text("header4", style = MnTheme.typography.header4)
        Text("header5", style = MnTheme.typography.header5)

        Text("bodySemibold", style = MnTheme.typography.bodySemiBold)
        Text("bodyRegular", style = MnTheme.typography.bodyRegular)

        Text("subBodySemibold", style = MnTheme.typography.subBodySemiBold)
        Text("subBodyRegular", style = MnTheme.typography.subBodyRegular)

        Text("captionSemibold", style = MnTheme.typography.captionSemiBold)
        Text("captionSemibold", style = MnTheme.typography.captionSemiBold)
    }
}
