package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun StatisticsContentHeader(
    time: String, // 데이터 형식 어떻게 뽑을지 고민 중
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
        modifier = modifier,
    ) {
        MnSmallIcon(
            resourceId = R.drawable.ic_circle,
            modifier = modifier,
            tint = MnTheme.iconColorScheme.disabled,
        )

        Text(
            text = time,
            style = MnTheme.typography.subBodyRegular,
            color = MnTheme.textColorScheme.tertiary,
        )
    }
}

@Preview
@Composable
private fun StatisticsContentHeaderPreview() {
    MnTheme {
        StatisticsContentHeader("11:58-13:32")
    }
}
