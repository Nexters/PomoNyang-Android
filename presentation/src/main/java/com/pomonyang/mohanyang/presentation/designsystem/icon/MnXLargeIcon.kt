package com.pomonyang.mohanyang.presentation.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.pomonyang.mohanyang.presentation.designsystem.token.MnIconSize

@Composable
fun MnXLargeIcon(
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = painterResource(id = resourceId),
        contentDescription = contentDescription,
        modifier = modifier.size(MnIconSize.xLarge),
        tint = tint
    )
}

@Composable
fun MnXLargeIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(MnIconSize.xLarge),
        tint = tint
    )
}
