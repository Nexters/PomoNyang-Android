package com.pomonyang.mohanyang.presentation.designsystem.button.select

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun MnSelectListItem(
    iconResource: Int,
    categoryType: String,
    focusTime: String,
    restTime: String,
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
                TimeComponent(
                    type = stringResource(R.string.focus),
                    time = focusTime,
                )
                TimeDivider()
                TimeComponent(
                    type = stringResource(R.string.rest),
                    time = restTime,
                )
            }
        },
        onClick = onClick,
    )
}

@Composable
private fun TimeDivider() {
    Box(
        Modifier
            .padding(horizontal = 4.dp)
            .width(1.dp)
            .height(12.dp)
            .background(
                color = MnTheme.iconColorScheme.tertiary,
                shape = RoundedCornerShape(MnRadius.xSmall),
            ),
    )
}

@Composable
private fun TimeComponent(
    type: String,
    time: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        Text(
            text = type,
            style = MnTheme.typography.subBodyRegular,
            color = MnTheme.textColorScheme.tertiary,
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = time,
            style = MnTheme.typography.subBodyRegular,
            color = MnTheme.textColorScheme.tertiary,
        )
    }
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
            restTime = "25",
            focusTime = "25",
            modifier = Modifier.fillMaxWidth(),
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "공부",
            onClick = {},
            restTime = "25",
            focusTime = "25",
            modifier = Modifier.fillMaxWidth(),
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "작업",
            onClick = {},
            restTime = "25",
            focusTime = "25",
            modifier = Modifier.fillMaxWidth(),
        )
        MnSelectListItem(
            iconResource = R.drawable.ic_null,
            categoryType = "운동",
            onClick = {},
            restTime = "25",
            focusTime = "25",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
