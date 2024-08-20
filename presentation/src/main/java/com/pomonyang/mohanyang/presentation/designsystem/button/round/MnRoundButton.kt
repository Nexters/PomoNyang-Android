package com.pomonyang.mohanyang.presentation.designsystem.button.round

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.common.MnPressableWrapper
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnLargeIcon
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
fun MnRoundButton(
    colors: MnRoundButtonColors,
    @DrawableRes iconResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerPadding: PaddingValues = PaddingValues()
) {
    val buttonSize = 88.dp

    MnPressableWrapper(
        modifier = Modifier.padding(containerPadding).clip(CircleShape),
        onClick = onClick
    ) {
        Box(
            modifier = modifier
                .size(buttonSize)
                .background(colors.containerColor),
            contentAlignment = Alignment.Center
        ) {
            MnLargeIcon(
                resourceId = iconResourceId,
                tint = colors.iconColor
            )
        }
    }
}

@ThemePreviews
@Composable
fun PreviewRoundButton() {
    MnTheme {
        MnRoundButton(
            colors = MnRoundButtonColorType.primary,
            onClick = {},
            iconResourceId = R.drawable.ic_null
        )
    }
}
