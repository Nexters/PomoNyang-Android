package com.pomonyang.mohanyang.presentation.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
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
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

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
data class MohaNyangTypography(
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
    val bodySemibold: TextStyle = TextStyle(
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

    val subBodySemibold: TextStyle = TextStyle(
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
    val captionSemibold: TextStyle = TextStyle(
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

internal val MohaNyangLocalTypo = staticCompositionLocalOf { MohaNyangTypography() }

@ThemePreviews
@DevicePreviews
@Composable
fun TypoPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    ) {
        Text("time", style = MohaNyangTheme.typography.time)

        Text("header1", style = MohaNyangTheme.typography.header1)
        Text("header2", style = MohaNyangTheme.typography.header2)
        Text("header3", style = MohaNyangTheme.typography.header3)
        Text("header4", style = MohaNyangTheme.typography.header4)
        Text("header5", style = MohaNyangTheme.typography.header5)

        Text("bodySemibold", style = MohaNyangTheme.typography.bodySemibold)
        Text("bodyRegular", style = MohaNyangTheme.typography.bodyRegular)

        Text("subBodySemibold", style = MohaNyangTheme.typography.subBodySemibold)
        Text("subBodyRegular", style = MohaNyangTheme.typography.subBodyRegular)

        Text("captionSemibold", style = MohaNyangTheme.typography.captionSemibold)
        Text("captionSemibold", style = MohaNyangTheme.typography.captionSemibold)
    }
}
