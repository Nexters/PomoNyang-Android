package com.pomonyang.mohanyang.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun CategoryBox(
    categoryName: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MnTheme.backgroundColorScheme.secondary,
    @DrawableRes iconRes: Int = R.drawable.ic_null,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(MnRadius.xSmall),
            )
            .padding(horizontal = MnSpacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        MnMediumIcon(
            resourceId = iconRes,
            tint = Color.Unspecified,
        )
        Text(
            text = categoryName,
            style = MnTheme.typography.subBodySemiBold,
            color = MnTheme.textColorScheme.tertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun CategoryBoxPreview() {
    MnTheme {
        CategoryBox(categoryName = "운동")
    }
}
