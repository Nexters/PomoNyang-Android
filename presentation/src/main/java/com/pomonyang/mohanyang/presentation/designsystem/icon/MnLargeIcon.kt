package com.pomonyang.mohanyang.presentation.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.pomonyang.mohanyang.presentation.designsystem.token.MnIconSize

@Composable
fun MnLargeIcon(
    modifier: Modifier = Modifier,
    @DrawableRes resourceId: Int,
    contentDescription: String? = null,
    tint: Color = Color.Unspecified
) {
    Icon(
        painter = painterResource(id = resourceId),
        contentDescription = contentDescription,
        modifier = modifier.size(MnIconSize.large),
        tint = tint
    )
}
