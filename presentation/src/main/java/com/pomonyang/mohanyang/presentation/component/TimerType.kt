package com.pomonyang.mohanyang.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
fun TimerType(
    modifier: Modifier = Modifier,
    type: String
) {
    Row(
        modifier = modifier.padding(top = MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MnSmallIcon(resourceId = R.drawable.ic_null)
        Text(
            modifier = Modifier.padding(MnSpacing.xSmall),
            text = type,
            style = MnTheme.typography.header5,
            color = MnTheme.textColorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun TimerTypePreview() {
    MnTheme {
        TimerType(type = "집중시간")
    }
}
