package com.pomonyang.mohanyang.presentation.designsystem.button.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.common.MnPressableWrapper
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
fun MnIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconResourceId: Int,
    iconColor: Color = LocalContentColor.current
) {
    val buttonSize = 40.dp

    MnPressableWrapper(
        modifier = modifier
            .clip(CircleShape),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.size(buttonSize),
            contentAlignment = Alignment.Center
        ) {
            MnMediumIcon(
                resourceId = iconResourceId,
                tint = iconColor
            )
        }
    }
}

@ThemePreviews
@Composable
fun PreviewIconButton() {
    MnTheme {
        CompositionLocalProvider(LocalContentColor provides MnTheme.backgroundColorScheme.accent1) {
            Column {
                MnIconButton(onClick = {}, iconResourceId = R.drawable.ic_null)
                MnIconButton(onClick = {}, iconResourceId = R.drawable.ic_null, iconColor = MnColor.Gray400)
            }
        }
    }
}
