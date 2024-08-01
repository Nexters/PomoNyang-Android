package com.pomonyang.mohanyang.presentation.designsystem.icon

import IconSize
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mohanyang.presentation.R

@Composable
fun MnMediumIcon(
    modifier: Modifier = Modifier,
    @DrawableRes resourceId: Int = R.drawable.ic_null,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = painterResource(id = resourceId),
        contentDescription = contentDescription,
        modifier = modifier.size(IconSize.medium),
        tint = tint
    )
}
