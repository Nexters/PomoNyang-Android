package com.pomonyang.mohanyang.presentation.designsystem.button.select

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.token.MnStroke
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import com.pomonyang.mohanyang.presentation.util.noRippleClickable

@Composable
fun MnSelectButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    isSelected: Boolean = false,
    isEnabled: Boolean = true,
    @DrawableRes leftIconResourceId: Int? = null,
    @DrawableRes rightIconResourceId: Int? = null,
    titleContent: @Composable (() -> Unit) = {},
    subTitleContent: @Composable (() -> Unit) = {},
) {
    val colors = when {
        !isEnabled -> MnSelectButtonSelector.disabled
        isSelected -> MnSelectButtonSelector.selected
        else -> MnSelectButtonSelector.default
    }

    Column(
        modifier = modifier
            .border(
                border = BorderStroke(
                    MnStroke.small,
                    colors.borderColor,
                ),
                shape = RoundedCornerShape(MnRadius.xSmall),
            )
            .background(
                color = colors.containerColor,
                shape = RoundedCornerShape(MnRadius.xSmall),
            )
            .noRippleClickable { onClick() }
            .semantics {
                role = Role.Button
            }
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leftIconResourceId != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.iconColor,
                ) {
                    MnSmallIcon(resourceId = leftIconResourceId)
                }
            }
            CompositionLocalProvider(
                LocalContentColor provides colors.subtitleContentColor,
                LocalTextStyle provides MnTheme.typography.subBodyRegular,
            ) {
                subTitleContent()
            }

            if (rightIconResourceId != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.iconColor,
                ) {
                    MnSmallIcon(resourceId = rightIconResourceId)
                }
            }
        }

        CompositionLocalProvider(
            LocalContentColor provides colors.titleContentColor,
            LocalTextStyle provides MnTheme.typography.header5,
        ) {
            titleContent()
        }
    }
}

@ThemePreviews
@Composable
fun PreviewMnSelectButton() {
    var selected by remember {
        mutableStateOf(false)
    }
    MnTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MnSelectButton(
                contentPadding = PaddingValues(
                    vertical = MnSpacing.medium,
                    horizontal = MnSpacing.small,
                ),
                isSelected = selected,
                onClick = { selected = !selected },
                leftIconResourceId = R.drawable.ic_null,
                subTitleContent = {
                    Text(text = "subTitle")
                },
            )

            MnSelectButton(
                contentPadding = PaddingValues(
                    vertical = MnSpacing.medium,
                    horizontal = MnSpacing.large,
                ),
                isSelected = selected,
                isEnabled = false,
                onClick = { selected = !selected },
                subTitleContent = {
                    Text(text = "hihi")
                },
                titleContent = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "button")
                    }
                },
                rightIconResourceId = R.drawable.ic_null,
            )
        }
    }
}
