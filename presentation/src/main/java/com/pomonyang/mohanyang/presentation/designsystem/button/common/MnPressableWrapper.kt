package com.pomonyang.mohanyang.presentation.designsystem.button.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import com.pomonyang.mohanyang.presentation.designsystem.token.MnInteraction
import com.pomonyang.mohanyang.presentation.util.clickableSingle

@Composable
fun MnPressableWrapper(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val clickableModifier = if (isEnabled) {
        Modifier
            .pressClickEffect(onClick)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .wrapContentSize(),
    ) {
        content()
        Box(
            modifier = clickableModifier
                .matchParentSize(),
        )
    }
}

enum class ButtonState { Pressed, Idle }

fun Modifier.pressClickEffect(onClick: () -> Unit) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    val interactionColor by animateColorAsState(
        targetValue = if (buttonState == ButtonState.Pressed) MnInteraction.pressed else MnInteraction.default,
        animationSpec = tween(100, 0),
        label = "button-interaction",
    )

    this
        .background(interactionColor)
        .clickableSingle(activeRippleEffect = false) { onClick() }
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}
