package com.pomonyang.mohanyang.presentation.util

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(throttleTime: Long = 500L): MultipleEventsCutter =
    MultipleEventsCutterImpl(throttleTime)

@Immutable
private class MultipleEventsCutterImpl(val throttleTime: Long) : MultipleEventsCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= throttleTime) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    activeRippleEffect: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickableSingle"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        role = role,
        indication = if (activeRippleEffect) LocalIndication.current else null,
        interactionSource = remember { MutableInteractionSource() }
    )
}
