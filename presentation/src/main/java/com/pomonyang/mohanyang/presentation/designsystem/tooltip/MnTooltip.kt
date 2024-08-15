package com.pomonyang.mohanyang.presentation.designsystem.tooltip

import android.view.Window
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.dpToPx
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import com.pomonyang.mohanyang.presentation.util.pxToDp
import kotlin.math.min
import kotlinx.coroutines.delay

/**
 * 텍스트 툴팁을 표시하기 위한 Modifier 함수입니다.
 *
 * @param content 툴팁에 표시할 텍스트.
 * @param tooltipColors 툴팁의 색상을 정의하는 MnTooltipColors 객체. 기본값은 MnTooltipDefaults.lightTooltipColors().
 * @param horizontalAlignment 툴팁의 수평 정렬. 기본값은 Alignment.CenterHorizontally.
 * @param verticalAlignment 툴팁의 수직 정렬. 기본값은 Alignment.Top.
 * @param anchorAlignment 툴팁 앵커의 정렬. 기본값은 Alignment.BottomCenter.
 * @param anchorPadding 앵커 주변의 패딩. 기본값은 PaddingValues().
 * @param contentAlign 툴팁 텍스트의 정렬. 기본값은 TextAlign.Center.
 * @param enabled 툴팁이 활성화되어 있는지 여부. 기본값은 true.
 */
@Composable
fun Modifier.tooltip(
    content: String,
    modifier: Modifier = Modifier,
    tooltipColors: MnTooltipColors = MnTooltipDefaults.lightTooltipColors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    anchorAlignment: Alignment = Alignment.BottomCenter,
    tooltipPadding: PaddingValues = PaddingValues(),
    anchorPadding: PaddingValues = PaddingValues(),
    contentAlign: TextAlign = TextAlign.Center,
    enabled: Boolean = true
): Modifier {
    val density = LocalDensity.current
    var positionInRoot by remember { mutableStateOf(IntOffset.Zero) }
    var tooltipContentSize by remember { mutableStateOf(IntSize(0, 0)) }
    var componentSize by remember { mutableStateOf(IntSize(0, 0)) }
    val transition = updateTransition(
        targetState = enabled,
        label = "tooltip transition"
    )
    if (enabled) {
        Popup(
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    val offset = calculateOffset(
                        positionInRoot = positionInRoot,
                        componentSize = componentSize,
                        tooltipSize = popupContentSize,
                        screenWidthPx = windowSize.width,
                        screenHeightPx = windowSize.height,
                        horizontalAlignment = horizontalAlignment,
                        verticalAlignment = verticalAlignment
                    )
                    val horizontalPadding = with(density) {
                        tooltipPadding.calculateLeftPadding(layoutDirection).roundToPx() -
                            tooltipPadding.calculateRightPadding(layoutDirection).roundToPx()
                    }
                    val verticalPadding = with(density) {
                        tooltipPadding.calculateTopPadding().roundToPx() -
                            tooltipPadding.calculateBottomPadding().roundToPx()
                    }

                    return IntOffset(
                        x = offset.x + horizontalPadding,
                        y = offset.y + verticalPadding
                    )
                }
            }
        ) {
            MnTooltipImpl(
                modifier = modifier
                    .onSizeChanged { tooltipContentSize = it }
                    .animateTooltip(transition),
                tooltipColors = tooltipColors,
                tooltipContentSize = tooltipContentSize,
                verticalAlignment = verticalAlignment,
                content = content,
                contentAlign = contentAlign,
                arrowPadding = anchorPadding,
                arrowAlignment = anchorAlignment
            )
        }
    }

    return this then Modifier.onPlaced {
        componentSize = it.size
        positionInRoot = it.positionInWindow().run {
            IntOffset(this.x.toInt(), this.y.toInt())
        }
    }
}

/**
 * 온보딩 및 가이드에 사용되는 Toolti을 위한 Modifier 함수입니다.
 *
 * @see com.pomonyang.mohanyang.presentation.designsystem.tooltip.MnBottomTooltipPreview
 *
 * @param content 툴팁에 표시할 텍스트.
 * @param tooltipColors 툴팁의 색상을 정의하는 MnTooltipColors 객체. 기본값은 MnTooltipDefaults.lightTooltipColors().
 * @param horizontalAlignment 툴팁의 수평 정렬. 기본값은 Alignment.CenterHorizontally.
 * @param verticalAlignment 툴팁의 수직 정렬. 기본값은 Alignment.Top.
 * @param anchorAlignment 툴팁 앵커의 정렬. 기본값은 Alignment.BottomCenter.
 * @param anchorPadding 앵커 주변의 패딩. 기본값은 PaddingValues().
 * @param ovalShape 강조 영역의 모양을 정의하는 Dp 값. 기본값은 0.dp.
 * @param contentAlign 툴팁 텍스트의 정렬. 기본값은 TextAlign.Center.
 * @param enabled 툴팁이 활성화되어 있는지 여부. 기본값은 true.
 * @param showOverlay 오버레이를 표시할지 여부. 기본값은 true.
 * @param highlightComponent 컴포넌트를 강조할지 여부. 기본값은 true.
 * @param onDismiss 툴팁이 해제될 때 호출되는 콜백 함수.
 */
@Composable
fun Modifier.guideTooltip(
    content: String,
    modifier: Modifier = Modifier,
    tooltipColors: MnTooltipColors = MnTooltipDefaults.lightTooltipColors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    anchorAlignment: Alignment = Alignment.BottomCenter,
    anchorPadding: PaddingValues = PaddingValues(),
    ovalShape: Dp = 0.dp,
    contentAlign: TextAlign = TextAlign.Center,
    enabled: Boolean = true,
    showOverlay: Boolean = true,
    highlightComponent: Boolean = true,
    onDismiss: () -> Unit = {}
): Modifier {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val activity = LocalContext.current as? ComponentActivity
    val screenWidthPx = remember { with(density) { configuration.screenWidthDp.dp.roundToPx() } }
    val screenHeightPx = remember { with(density) { configuration.screenHeightDp.dp.roundToPx() } }

    var positionInRoot by remember { mutableStateOf(IntOffset.Zero) }
    var tooltipContentSize by remember { mutableStateOf(IntSize(0, 0)) }
    var componentSize by remember { mutableStateOf(IntSize(0, 0)) }
    val statusBarHeight = with(density) { WindowInsets.systemBars.getTop(this) }

    val tooltipOffset by remember(positionInRoot, componentSize, tooltipContentSize) {
        derivedStateOf {
            calculateOffset(
                positionInRoot = positionInRoot,
                componentSize = componentSize,
                tooltipSize = tooltipContentSize,
                screenWidthPx = screenWidthPx,
                screenHeightPx = screenHeightPx,
                horizontalAlignment = horizontalAlignment,
                verticalAlignment = verticalAlignment
            )
        }
    }
    val transition = updateTransition(
        targetState = enabled,
        label = "tooltip transition"
    )
    if (enabled) {
        if (showOverlay) {
            HandleSystemBarsColor(window = activity?.window)
        }

        Popup {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .drawOverlayBackground(
                        showOverlay = showOverlay,
                        highlightComponent = highlightComponent,
                        positionInRoot = positionInRoot,
                        componentSize = componentSize,
                        backgroundColor = MnTooltipDefaults.overlayBackgroundColor,
                        shape = ovalShape
                    )
                    .noRippleClickable { onDismiss() }
            ) {
                MnTooltipImpl(
                    modifier = Modifier
                        .onSizeChanged { tooltipContentSize = it }
                        .offset { tooltipOffset }
                        .animateTooltip(transition),
                    tooltipColors = tooltipColors,
                    tooltipContentSize = tooltipContentSize,
                    verticalAlignment = verticalAlignment,
                    content = content,
                    contentAlign = contentAlign,
                    arrowPadding = anchorPadding,
                    arrowAlignment = anchorAlignment
                )
            }
        }
    }

    return this then Modifier.onPlaced {
        componentSize = it.size
        positionInRoot = it.positionInWindow().run {
            IntOffset(this.x.toInt(), this.y.toInt() - statusBarHeight)
        }
    }
}

@Composable
private fun HandleSystemBarsColor(window: Window?) {
    LaunchedEffect(Unit) {
        delay(1)
        window?.statusBarColor = MnTooltipDefaults.overlayBackgroundColor.toArgb()
        window?.navigationBarColor = MnTooltipDefaults.overlayBackgroundColor.toArgb()
    }
    DisposableEffect(Unit) {
        onDispose {
            window?.statusBarColor = Color.Transparent.toArgb()
            window?.navigationBarColor = Color.Transparent.toArgb()
        }
    }
}

@Composable
private fun MnTooltipImpl(
    tooltipColors: MnTooltipColors,
    tooltipContentSize: IntSize,
    verticalAlignment: Alignment.Vertical,
    content: String,
    contentAlign: TextAlign,
    arrowPadding: PaddingValues,
    arrowAlignment: Alignment,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (verticalAlignment) {
            Alignment.Top -> {
                TooltipContent(
                    tooltipColors = tooltipColors,
                    content = content,
                    contentAlign = contentAlign
                )

                TooltipAnchor(
                    modifier = Modifier
                        .padding(arrowPadding)
                        .width(
                            tooltipContentSize.width
                                .toFloat()
                                .pxToDp()
                        ),
                    anchorColor = tooltipColors.containerColor,
                    direction = arrowAlignment
                )
            }

            Alignment.Bottom -> {
                TooltipAnchor(
                    modifier = Modifier
                        .padding(arrowPadding)
                        .width(
                            tooltipContentSize.width
                                .toFloat()
                                .pxToDp()
                        ),
                    anchorColor = tooltipColors.containerColor,
                    direction = arrowAlignment
                )

                TooltipContent(
                    tooltipColors = tooltipColors,
                    content = content,
                    contentAlign = contentAlign
                )
            }
        }
    }
}

@Composable
private fun TooltipContent(
    tooltipColors: MnTooltipColors,
    content: String,
    contentAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    Text(
        style = MnTheme.typography.bodySemiBold,
        modifier = modifier
            .background(
                color = tooltipColors.containerColor,
                shape = RoundedCornerShape(MnRadius.xSmall)
            )
            .padding(
                vertical = MnSpacing.medium,
                horizontal = MnSpacing.large
            ),
        text = content,
        textAlign = contentAlign,
        color = tooltipColors.contentColor
    )
}

@Composable
private fun TooltipAnchor(
    anchorColor: Color,
    modifier: Modifier = Modifier,
    direction: Alignment = Alignment.BottomCenter
) {
    val triangleWidth = MnTooltipDefaults.anchorWidth.dpToPx()
    val triangleHeight = MnTooltipDefaults.anchorHeight.dpToPx()

    Canvas(modifier = modifier.height(triangleHeight.pxToDp())) {
        val path = Path().apply {
            when (direction) {
                Alignment.TopCenter -> {
                    moveTo(size.width / 2f - triangleWidth / 2f, size.height)
                    lineTo(size.width / 2f, 0f)
                    lineTo(size.width / 2f + triangleWidth / 2f, size.height)
                }

                Alignment.TopStart -> {
                    moveTo(0f, size.height)
                    lineTo(triangleWidth / 2f, 0f)
                    lineTo(triangleWidth, size.height)
                }

                Alignment.BottomCenter -> {
                    moveTo(size.width / 2f - triangleWidth / 2f, 0f)
                    lineTo(size.width / 2f, size.height)
                    lineTo(size.width / 2f + triangleWidth / 2f, 0f)
                }

                Alignment.BottomStart -> {
                    moveTo(0f, 0f)
                    lineTo(triangleWidth / 2f, size.height)
                    lineTo(triangleWidth, 0f)
                }

                else -> {
                    moveTo(size.width / 2f - triangleWidth / 2f, 0f)
                    lineTo(size.width / 2f, size.height)
                    lineTo(size.width / 2f + triangleWidth / 2f, 0f)
                }
            }
            close()
        }
        drawPath(path = path, color = anchorColor)
    }
}

private fun calculateOffset(
    positionInRoot: IntOffset,
    componentSize: IntSize,
    tooltipSize: IntSize,
    screenWidthPx: Int,
    screenHeightPx: Int,
    horizontalAlignment: Alignment.Horizontal,
    verticalAlignment: Alignment.Vertical
): IntOffset {
    val horizontalAlignmentPosition = when (horizontalAlignment) {
        Alignment.Start -> positionInRoot.x
        Alignment.End -> positionInRoot.x + componentSize.width - tooltipSize.width
        else -> positionInRoot.x + (componentSize.width / 2) - (tooltipSize.width / 2)
    }
    val verticalAlignmentPosition = when (verticalAlignment) {
        Alignment.Top -> positionInRoot.y - tooltipSize.height
        Alignment.Bottom -> positionInRoot.y + componentSize.height
        else -> positionInRoot.y + (componentSize.height / 2)
    }
    return IntOffset(
        x = min(screenWidthPx - tooltipSize.width, horizontalAlignmentPosition),
        y = min(screenHeightPx - tooltipSize.height, verticalAlignmentPosition)
    )
}

private fun Modifier.drawOverlayBackground(
    showOverlay: Boolean,
    highlightComponent: Boolean,
    positionInRoot: IntOffset,
    componentSize: IntSize,
    backgroundColor: Color,
    shape: Dp
): Modifier = if (showOverlay) {
    if (highlightComponent) {
        drawBehind {
            val highlightPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(positionInRoot.toOffset(), componentSize.toSize()),
                        cornerRadius = CornerRadius(shape.toPx())
                    )
                )
            }
            clipPath(highlightPath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(backgroundColor))
            }
        }
    } else {
        background(backgroundColor)
    }
} else {
    this
}

private fun Modifier.animateTooltip(
    transition: Transition<Boolean>
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "animateTooltip"
        properties["transition"] = transition
    }
) {
    val commonTransitionSpec = tween<Float>(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    )

    val scale by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                commonTransitionSpec
            } else {
                commonTransitionSpec
            }
        },
        label = "tooltip_transition_scaling"
    ) { if (it) 1f else 0f }

    val alpha by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                commonTransitionSpec
            } else {
                commonTransitionSpec
            }
        },
        label = "tooltip_transition_alpha"
    ) { if (it) 1f else 0f }

    this.graphicsLayer(
        scaleX = scale,
        scaleY = scale,
        alpha = alpha,
        transformOrigin = TransformOrigin.Center // 중심으로부터 스케일링
    )
}

@Composable
@Preview
private fun MnTopTooltipPreview() {
    MnTheme {
        var isShowTooltip by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sample",
                modifier = Modifier
                    .noRippleClickable { isShowTooltip = true }
                    .guideTooltip(
                        anchorPadding = PaddingValues(bottom = 12.dp),
                        verticalAlignment = Alignment.Top,
                        enabled = isShowTooltip,
                        content = "눌러서 시간을 조정할 수 있어요",
                        ovalShape = 12.dp,
                        onDismiss = {
                            isShowTooltip = false
                        }
                    )
                    .background(color = MnTheme.backgroundColorScheme.secondary, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 9.5.dp, horizontal = 24.dp),
                fontSize = 24.sp
            )
        }
    }
}

@Composable
@Preview
private fun MnBottomTooltipPreview() {
    MnTheme {
        var isShowTooltip by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Sample",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .noRippleClickable { isShowTooltip = true }
                    .guideTooltip(
                        tooltipColors = MnTooltipDefaults.darkTooltipColors(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalAlignment = Alignment.Start,
                        anchorAlignment = Alignment.TopStart,
                        anchorPadding = PaddingValues(start = 30.dp, top = 12.dp),
                        enabled = isShowTooltip,
                        content = "눌러서 카테고리를 변경할 수 있어요",
                        ovalShape = 12.dp,
                        onDismiss = {
                            isShowTooltip = false
                        }
                    )
                    .background(color = MnTheme.backgroundColorScheme.secondary, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 9.5.dp, horizontal = 24.dp),
                fontSize = 24.sp
            )
        }
    }
}
