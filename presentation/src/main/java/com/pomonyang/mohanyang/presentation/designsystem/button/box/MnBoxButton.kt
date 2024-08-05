package com.pomonyang.mohanyang.presentation.designsystem.button.box

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.common.MnButtonStyleProperties
import com.pomonyang.mohanyang.presentation.designsystem.button.common.MnPressableWrapper
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
fun MnBoxButton(
    modifier: Modifier = Modifier,
    text: String = "",
    colors: MnBoxButtonColors,
    styles: MnButtonStyleProperties,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    @DrawableRes rightIconResourceId: Int? = null,
    @DrawableRes leftIconResourceId: Int? = null
) {
    val containerColor = if (isEnabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (isEnabled) colors.contentColor else colors.disabledContentColor
    val iconColor = if (isEnabled) colors.iconColor else colors.disabledIconColor

    MnPressableWrapper(
        modifier = Modifier.clip(shape = styles.shape),
        onClick = onClick
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides styles.textStyle
        ) {
            Box(
                modifier = modifier
                    .height(styles.height)
                    .background(
                        containerColor
                    )
                    .padding(styles.contentPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(styles.spacing)
                ) {
                    if (leftIconResourceId != null) MnMediumIcon(resourceId = leftIconResourceId, tint = iconColor)
                    Text(text = text, textAlign = TextAlign.Center)
                    if (rightIconResourceId != null) MnMediumIcon(resourceId = rightIconResourceId, tint = iconColor)
                }
            }
        }
    }
}

@ThemePreviews
@Composable
@Preview
fun PreviewMnBoxButton() {
    MnTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MnBoxButton(
                styles = MnBoxButtonStyles.large,
                text = "Button",
                onClick = { },
                colors = MnBoxButtonColorType.primary
            )
            MnBoxButton(
                styles = MnBoxButtonStyles.large,
                text = "Button",
                onClick = { },
                colors = MnBoxButtonColorType.primary,
                rightIconResourceId = R.drawable.ic_null,
                leftIconResourceId = R.drawable.ic_null
            )
            MnBoxButton(
                styles = MnBoxButtonStyles.large,
                isEnabled = false,
                text = "Button",
                onClick = { },
                colors = MnBoxButtonColorType.primary,
                rightIconResourceId = R.drawable.ic_null,
                leftIconResourceId = R.drawable.ic_null
            )
            MnBoxButton(
                styles = MnBoxButtonStyles.medium,
                text = "Button",
                onClick = {},
                colors = MnBoxButtonColorType.secondary,
                leftIconResourceId = R.drawable.ic_null
            )

            MnBoxButton(
                styles = MnBoxButtonStyles.small,
                text = "Button",
                onClick = {},
                colors = MnBoxButtonColorType.secondary
            )
            MnBoxButton(
                styles = MnBoxButtonStyles.small,
                text = "Button",
                onClick = {},
                leftIconResourceId = R.drawable.ic_null,
                colors = MnBoxButtonColorType.tertiary
            )
        }
    }
}
