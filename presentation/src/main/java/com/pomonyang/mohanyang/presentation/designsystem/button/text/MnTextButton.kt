package com.pomonyang.mohanyang.presentation.designsystem.button.text

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.contentcapture.ContentCaptureManager.Companion.isEnabled
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
import com.pomonyang.mohanyang.presentation.util.clickableSingle

@Composable
fun MnTextButton(
    text: String,
    styles: MnButtonStyleProperties,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerPadding: PaddingValues = PaddingValues(),
    isEnabled: Boolean = true,
    @DrawableRes rightIconResourceId: Int? = null,
    @DrawableRes leftIconResourceId: Int? = null,
) {
    MnPressableWrapper(
        modifier = Modifier.padding(containerPadding).clip(styles.shape),
        onClick = onClick,
    ) {
        Box(
            modifier = modifier
                .height(styles.height)
                .padding(styles.contentPadding),
            contentAlignment = Alignment.Center,

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(styles.spacing),
            ) {
                if (leftIconResourceId != null) {
                    MnMediumIcon(
                        resourceId = leftIconResourceId,
                        tint = if (isEnabled) MnTheme.iconColorScheme.secondary else MnTheme.iconColorScheme.tertiary,
                    )
                }
                Text(text = text, style = styles.textStyle, textAlign = TextAlign.Center, color = if (isEnabled) MnTheme.textColorScheme.secondary else MnTheme.textColorScheme.tertiary)
                if (rightIconResourceId != null) {
                    MnMediumIcon(
                        resourceId = rightIconResourceId,
                        tint = if (isEnabled) MnTheme.iconColorScheme.secondary else MnTheme.iconColorScheme.tertiary,
                    )
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
            MnTextButton(
                styles = MnTextButtonStyles.large,
                text = "Button",
                onClick = {},
            )
            MnTextButton(
                modifier = Modifier.clickableSingle(activeRippleEffect = false) { },
                styles = MnTextButtonStyles.large,
                text = "Button",
                onClick = {},
                isEnabled = false,
                rightIconResourceId = R.drawable.ic_null,
                leftIconResourceId = R.drawable.ic_null,
            )

            MnTextButton(
                modifier = Modifier.clickableSingle(activeRippleEffect = false) { },
                styles = MnTextButtonStyles.medium,
                text = "Button",
                onClick = {},
                isEnabled = false,
                rightIconResourceId = R.drawable.ic_null,
            )
            MnTextButton(
                styles = MnTextButtonStyles.small,
                text = "Button",
                onClick = {},
            )
        }
    }
}
