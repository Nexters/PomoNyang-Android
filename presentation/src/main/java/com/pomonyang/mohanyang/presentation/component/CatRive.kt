package com.pomonyang.mohanyang.presentation.component

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import app.rive.runtime.kotlin.RiveAnimationView
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.MnTooltipDefaults
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.TooltipAnchor
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.TooltipContent
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

@Composable
fun CatRive(
    @RawRes riveResource: Int,
    modifier: Modifier = Modifier,
    tooltipMessage: String? = null,
    isAutoPlay: Boolean = true,
    riveAnimationName: String? = null,
    stateMachineName: String? = null,
    stateMachineInput: String? = null,
    fireState: String? = null,
    onRiveClick: (RiveAnimationView) -> Unit = {},
) {
    var showTooltip by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val riveView = remember {
        RiveAnimationView(context)
    }

    LaunchedEffect(riveAnimationName) {
        riveView.setRiveResource(
            resId = riveResource,
            stateMachineName = stateMachineName,
            autoplay = isAutoPlay,
            animationName = riveAnimationName,
        )
    }

    LaunchedEffect(stateMachineInput) {
        if (stateMachineName != null && stateMachineInput != null) {
            riveView.reset()
            riveView.setBooleanState(stateMachineName, stateMachineInput, true)
        }
    }

    LaunchedEffect(fireState) {
        if (stateMachineName != null && fireState != null) {
            riveView.fireState(stateMachineName, fireState)
        }
    }

    if (tooltipMessage != null) {
        LaunchedEffect(tooltipMessage) {
            delay(0.5.seconds)
            showTooltip = true
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        Box {
            AndroidView(
                modifier = Modifier.size(240.dp),
                factory = { riveView },
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickableSingle(
                        activeRippleEffect = false,
                    ) {
                        onRiveClick(riveView)
                    },
            )
        }

        AnimatedVisibility(
            visible = showTooltip && tooltipMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            val colors = MnTooltipDefaults.lightTooltipColors()
            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .clickable(enabled = false) {},
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TooltipContent(
                    tooltipColors = MnTooltipDefaults.lightTooltipColors(),
                    content = tooltipMessage ?: "",
                    contentAlign = TextAlign.Center,
                )

                TooltipAnchor(
                    modifier = Modifier.width(MnTooltipDefaults.anchorWidth),
                    anchorColor = colors.containerColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CatRivePreview() {
    MnTheme {
        CatRive(
            tooltipMessage = "잘 집중하고 있는 거냥?",
            riveAnimationName = "Cheese Cat",
            riveResource = 0,
        )
    }
}

@Preview
@Composable
private fun CatRiveNamePreview() {
    MnTheme {
        CatRive(
            tooltipMessage = "잘 집중하고 있는 거냥?",
            riveResource = 0,
        )
    }
}
