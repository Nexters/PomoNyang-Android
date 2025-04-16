package com.pomonyang.mohanyang.presentation.designsystem.button.common

import MnToggleButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.button.round.MnRoundButton
import com.pomonyang.mohanyang.presentation.designsystem.button.round.MnRoundButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import timber.log.Timber

@Immutable
data class MnButtonStyleProperties(
    val height: Dp,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val spacing: Dp,
    val textStyle: TextStyle,
)

@ThemePreviews
@Composable
fun PreviewButtons() {
    MnTheme {
        var checked by remember { mutableStateOf(false) }
        var selected by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row {
                MnBoxButton(
                    styles = MnBoxButtonStyles.medium,
                    text = "Button",
                    onClick = { Timber.d("click primary") },
                    colors = MnBoxButtonColorType.primary,
                )
                MnBoxButton(
                    styles = MnBoxButtonStyles.medium,
                    isEnabled = false,
                    text = "Button",
                    onClick = { Timber.d("click primary disable") },
                    colors = MnBoxButtonColorType.primary,
                    rightIconResourceId = R.drawable.ic_null,
                    leftIconResourceId = R.drawable.ic_null,
                )
            }

            MnBoxButton(
                styles = MnBoxButtonStyles.small,
                text = "Button",
                onClick = {},
                colors = MnBoxButtonColorType.secondary,
                leftIconResourceId = R.drawable.ic_null,
            )

            MnBoxButton(
                styles = MnBoxButtonStyles.large,
                text = "Button",
                onClick = {},
                leftIconResourceId = R.drawable.ic_null,
                colors = MnBoxButtonColorType.tertiary,
            )
            Row {
                MnTextButton(
                    styles = MnTextButtonStyles.large,
                    text = "Button Disabled",
                    onClick = {},
                    isEnabled = false,
                    leftIconResourceId = R.drawable.ic_null,
                )
                MnTextButton(styles = MnTextButtonStyles.large, text = "textButton", onClick = {
                })
            }

            MnRoundButton(
                colors = MnRoundButtonColorType.secondary,
                iconResourceId = R.drawable.ic_null,
                onClick = {
                },
            )

            MnToggleButton(isChecked = checked, onCheckedChange = {
                checked = it
            })

            MnSelectButton(
                modifier = Modifier.padding(10.dp),
                onClick = { selected = !selected },
                isSelected = selected,
                titleContent = {
                    Text(text = "select button")
                },
            )
            MnSelectButton(
                modifier = Modifier.padding(10.dp),
                isSelected = selected,
                onClick = {},
                isEnabled = false,
                subTitleContent = {
                    Text(text = "subtitle")
                },
                titleContent = {
                    Text(text = "select button Disabled")
                },
                leftIconResourceId = R.drawable.ic_null,
            )
            MnIconButton(onClick = { }, iconResourceId = R.drawable.ic_null)
        }
    }
}
