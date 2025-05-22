package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
private fun StatisticsTopBar(
    month: String,
    day: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MnColor.Gray50)
            .padding(MnSpacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MnIconButton(
            iconResourceId = R.drawable.ic_chevron_left,
            onClick = onLeftClick,
        )

        StatisticsTopBarTitle(
            month = month,
            day = day,
            onMoreClick = onMoreClick,
        )

        MnIconButton(
            iconResourceId = R.drawable.ic_chevron_right,
            onClick = onRightClick,
        )
    }
}

@Composable
private fun StatisticsTopBarTitle(
    month: String,
    day: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            MnSpacing.xSmall,
        ),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.statistics_top_bar_title, month, day),
            style = MnTheme.typography.header5,
        )
        MnIconButton(
            iconResourceId = R.drawable.ic_chevron_down,
            iconColor = MnTheme.iconColorScheme.tertiary,
            onClick = onMoreClick,
            modifier = Modifier.background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = CircleShape,
            ),
        )
    }
}

@ThemePreviews
@Composable
private fun StatisticsTopBarPreview() {
    MnTheme {
        StatisticsTopBar(
            month = "3",
            day = "19",
            onLeftClick = {},
            onRightClick = {},
            onMoreClick = {},
        )
    }
}
