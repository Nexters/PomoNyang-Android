package com.pomonyang.mohanyang.presentation.designsystem.button.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    containerPadding: PaddingValues = PaddingValues(),
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    MnSelectButton(
        modifier = modifier,
        containerPadding = containerPadding,
        isSelected = isSelected,
        subTitleContent = {
            Row(
                modifier = Modifier
                    .padding(MnSpacing.xLarge)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MnMediumIcon(
                    resourceId = iconResource,
                    tint = Color.Unspecified,
                )

                Text(
                    modifier = Modifier.padding(
                        start = MnSpacing.small,
                        end = MnSpacing.medium,
                    ),
                    text = categoryType,
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary,
                )
            }
        },
        onClick = onClick,
    )
}

@Preview
@Composable
fun MnSelectListPreview(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "기본",
            onClick = {},
            isSelected = true,
            modifier = Modifier.fillMaxWidth(),
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "공부",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "작업",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "운동",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
