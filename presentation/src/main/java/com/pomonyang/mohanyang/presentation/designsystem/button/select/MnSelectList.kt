package com.pomonyang.mohanyang.presentation.designsystem.button.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun MnSelectListItem(
    iconResource: Int,
    categoryType: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isDisabled: Boolean = false,
    colors: MnSelectListColors = MnSelectListDefaults.colors(),
    onClick: () -> Unit,
) {
    MnSelectButton(
        modifier = modifier,
        isSelected = isSelected,
        isEnabled = isDisabled.not(),
        subTitleContent = {
            Row(
                modifier = Modifier.padding(MnSpacing.xLarge),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MnMediumIcon(
                    resourceId = iconResource,
                    tint = if (isDisabled) colors.disabledIconTint else colors.enabledIconTint,
                )

                Text(
                    modifier = Modifier.padding(
                        start = MnSpacing.small,
                        end = MnSpacing.medium,
                    ),
                    text = categoryType,
                    style = MnTheme.typography.bodySemiBold,
                    color = if (isDisabled) colors.disabledTextColor else colors.enabledTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.weight(1f))
            }
        },
        onClick = onClick,
    )
}

@Preview
@Composable
fun MnSelectListPreview(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "기본",
            onClick = {},
            isSelected = true,
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "공부",
            onClick = {},
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "작업",
            onClick = {},
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "운동",
            onClick = {},
        )
    }
}
