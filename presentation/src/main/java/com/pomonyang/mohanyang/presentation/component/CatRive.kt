package com.pomonyang.mohanyang.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.MnTooltipDefaults
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.TooltipAnchor
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.TooltipContent
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.noRippleClickable

@Composable
fun CatRive(
    modifier: Modifier = Modifier,
    tooltipMessage: String? = null,
    catName: String? = null,
    iconRes: Int? = null,
    onClick: () -> Unit = {}
) {
    val catRiveModifier = if (tooltipMessage != null) {
        Modifier.padding(top = MnSpacing.threeXLarge)
    } else {
        Modifier
    }
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        tooltipMessage?.let {
            val colors = MnTooltipDefaults.lightTooltipColors()
            Column(
                modifier = Modifier.zIndex(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TooltipContent(
                    tooltipColors = MnTooltipDefaults.lightTooltipColors(),
                    content = it,
                    contentAlign = TextAlign.Center
                )

                TooltipAnchor(
                    modifier = Modifier
                        .width(MnTooltipDefaults.anchorWidth),
                    anchorColor = colors.containerColor
                )
            }
        }

        Column(
            modifier = catRiveModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .size(240.dp)
                    .background(MnTheme.backgroundColorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "image")
            }

            Row(
                modifier = Modifier
                    .noRippleClickable { onClick() }
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall)
            ) {
                catName?.let {
                    Text(
                        text = it,
                        style = MnTheme.typography.header4,
                        color = MnTheme.textColorScheme.tertiary
                    )
                }
                iconRes?.let {
                    MnMediumIcon(resourceId = iconRes, tint = MnTheme.iconColorScheme.primary)
                }
            }
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
        CatRive(tooltipMessage = "잘 집중하고 있는 거냥?", catName = "치즈냥", iconRes = R.drawable.ic_null)
    }
}
