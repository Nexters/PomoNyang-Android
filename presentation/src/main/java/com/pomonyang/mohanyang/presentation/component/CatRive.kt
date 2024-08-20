package com.pomonyang.mohanyang.presentation.component

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import app.rive.runtime.kotlin.core.ExperimentalAssetLoader
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.MnTooltipDefaults
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.TooltipAnchor
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.TooltipContent
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

@OptIn(ExperimentalAssetLoader::class)
@Composable
fun CatRive(
    @RawRes riveResource: Int,
    modifier: Modifier = Modifier,
    tooltipMessage: String? = null,
    isAutoPlay: Boolean = true,
    riveAnimationName: String? = null,
    stateMachineName: String? = null,
    stateMachineInput: String? = null,
    onRiveClick: (RiveAnimationView) -> Unit = {}
) {
    val catRiveModifier = if (tooltipMessage != null) {
        Modifier.padding(top = MnSpacing.threeXLarge)
    } else {
        Modifier
    }
    var showTooltip by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val riveView = remember {
        RiveAnimationView(context = context)
    }

    LaunchedEffect(riveAnimationName) {
        riveView.setRiveResource(
            resId = riveResource,
            stateMachineName = stateMachineName,
            autoplay = isAutoPlay,
            animationName = riveAnimationName
        )
    }
    if (stateMachineName != null && stateMachineInput != null) {
        riveView.setBooleanState(stateMachineName, stateMachineInput, true)
        riveView.play()
    }

    if (tooltipMessage != null) {
        LaunchedEffect(tooltipMessage) {
            delay(0.5.seconds)
            showTooltip = true
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = showTooltip && tooltipMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val colors = MnTooltipDefaults.lightTooltipColors()
            Column(
                modifier = Modifier.zIndex(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TooltipContent(
                    tooltipColors = MnTooltipDefaults.lightTooltipColors(),
                    content = tooltipMessage ?: "",
                    contentAlign = TextAlign.Center
                )

                TooltipAnchor(
                    modifier = Modifier.width(MnTooltipDefaults.anchorWidth),
                    anchorColor = colors.containerColor
                )
            }
        }

        Column(
            modifier = catRiveModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(240.dp)
            ) {
                AndroidView(
                    modifier = Modifier.matchParentSize(),
                    factory = { riveView }
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickableSingle(
                            activeRippleEffect = false
                        ) {
                            onRiveClick(riveView)
                        }
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
            riveResource = 0
        )
    }
}

@Preview
@Composable
private fun CatRiveNamePreview() {
    MnTheme {
        CatRive(
            tooltipMessage = "잘 집중하고 있는 거냥?",
            riveResource = 0
        )
    }
}
