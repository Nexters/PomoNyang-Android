package com.pomonyang.mohanyang.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.tooltip
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.coroutines.delay

@Composable
fun CatRive(
    modifier: Modifier = Modifier,
    tooltipMessage: String? = null,
    catName: String? = null,
    showTooltip: Boolean = false
) {
    var tooltipEnabled by remember { mutableStateOf(showTooltip) }
    val tooltipModifier = if (tooltipEnabled && tooltipMessage != null) {
        Modifier.tooltip(
            enabled = showTooltip,
            tooltipPadding = PaddingValues(top = 20.dp),
            content = tooltipMessage
        )
    } else {
        Modifier
    }

    LaunchedEffect(tooltipMessage) {
        if (tooltipMessage != null) {
            tooltipEnabled = false
            delay(50)
            tooltipEnabled = true
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(240.dp)
                .background(MnTheme.backgroundColorScheme.secondary)
                .then(tooltipModifier),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "image")
        }

        catName?.let {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = it,
                style = MnTheme.typography.header4,
                color = MnTheme.textColorScheme.tertiary
            )
        }
    }
}

@Preview
@Composable
private fun CatRivePreview() {
    MnTheme {
        CatRive(tooltipMessage = "잘 집중하고 있는 거냥?")
    }
}

@Preview
@Composable
private fun CatRiveNamePreview() {
    MnTheme {
        CatRive(tooltipMessage = "잘 집중하고 있는 거냥?", catName = "치즈냥")
    }
}
