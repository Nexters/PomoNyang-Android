package com.pomonyang.mohanyang.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

/**
 * 클릭 시 리플 효과가 없는 Modifier를 반환합니다.
 */
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            onClick()
        }
    }

/**
 * 클릭 이벤트를 디바운스 처리하여 여러 번 빠르게 클릭하는 것을 방지하고,
 * @see noRippleClickable
 * @param debounceTime 클릭 이벤트를 디바운스 처리할 시간 간격(밀리초). 기본값은 500ms.
 */
fun Modifier.debounceNoRippleClickable(
    debounceTime: Long = 500L,
    onClick: () -> Unit
): Modifier =
    composed {
        debounceHandler(debounceTime = debounceTime) { debouncedOnClick ->
            noRippleClickable {
                debouncedOnClick(onClick)
            }
        }
    }

/**
 * 클릭 이벤트를 디바운스 처리하여 여러 번 빠르게 클릭하는 것을 방지하는 Modifier를 반환합니다.
 *
 * @param debounceTime 클릭 이벤트를 디바운스 처리할 시간 간격(밀리초). 기본값은 500ms.
 */
fun Modifier.debounceClickable(
    debounceTime: Long = 500L,
    onClick: () -> Unit
): Modifier =
    composed {
        debounceHandler(debounceTime = debounceTime) { debouncedOnClick ->
            clickable {
                debouncedOnClick(onClick)
            }
        }
    }

@Composable
private fun <T> debounceHandler(
    debounceTime: Long,
    content: @Composable (DebouncedOnClick) -> T
): T {
    val eventFlow =
        remember {
            MutableSharedFlow<() -> Unit>(
                replay = 0,
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        }

    val result =
        content { event ->
            eventFlow.tryEmit(event)
        }

    LaunchedEffect(Unit) {
        eventFlow
            .debounce(debounceTime)
            .collect { event ->
                event()
            }
    }

    return result
}

typealias DebouncedOnClick = (() -> Unit) -> Unit
