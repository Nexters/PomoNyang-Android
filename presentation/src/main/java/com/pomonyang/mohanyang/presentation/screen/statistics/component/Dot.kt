package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun Dot(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(
                width = 1.dp,
                height = 5.dp,
            )
            .background(
                color = MnTheme.iconColorScheme.disabled,
                shape = RoundedCornerShape(50.dp),
            ),
    )
}

@Preview
@Composable
private fun DotPreview() {
    val repeatCount = 100
    MnTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            repeat(repeatCount) {
                Dot()
            }
        }
    }
}
