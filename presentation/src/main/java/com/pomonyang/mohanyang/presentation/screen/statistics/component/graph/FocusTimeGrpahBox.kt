package com.pomonyang.mohanyang.presentation.screen.statistics.component.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.spToPx
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay

@Composable
fun GraphContainer(
    graphData: ImmutableList<Float>,
    modifier: Modifier = Modifier,
    configure: FocusGraphConfigure = remember {
        FocusGraphConfigure(
            maxFocusTime = graphData.max(),
            targetDateTime = LocalDateTime.now(),
        )
    },
) {
    val graphDayParser = remember { DateTimeFormatter.ofPattern("M/dd") }
    val totalFocusTime = remember(graphData) { graphData.sum() }

    val canvasHeight = 160.dp
    val gap = (configure.maxYAxis.inWholeMinutes / configure.yAxisRange.inWholeMinutes).toInt()
    val gapHeight = canvasHeight / gap

    Column(
        modifier = modifier
            .background(MnColor.White, shape = RoundedCornerShape(MnRadius.small))
            .padding(MnSpacing.large),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = stringResource(R.string.graph_total_time, totalFocusTime.toInt() / 60, totalFocusTime.toInt() % 60),
            style = MnTheme.typography.header4,
            color = MnTheme.iconColorScheme.secondary,
        )
        GraphData(
            modifier = Modifier
                .padding(top = 48.dp, bottom = MnSpacing.xLarge)
                .height(canvasHeight),
            configure = configure,
            graphData = graphData,
            graphDayParser = graphDayParser,
            gap = gap,
            gapHeight = gapHeight,
        )
    }
}

@Composable
private fun FocusGraphXAxis(
    modifier: Modifier,
    graphData: List<Float>,
    configure: FocusGraphConfigure,
    parser: DateTimeFormatter,
    gapHeight: Dp,
) {
    val maxFocusTime = remember(graphData) { graphData.max() }

    var barAnimated by remember { mutableStateOf(false) }
    var showTooltip by remember { mutableStateOf(false) }
    val totalAnimationDuration = 1000 + (graphData.size - 1) * 100L

    LaunchedEffect(graphData) {
        barAnimated = true
        delay(totalAnimationDuration)
        showTooltip = true
    }

    Box(
        modifier = modifier.padding(horizontal = MnSpacing.xSmall),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            graphData.forEachIndexed { idx, data ->
                key(idx) {
                    val barHeight = if (data == 0f) MnSpacing.xSmall else gapHeight * (data / configure.yAxisRange.inWholeMinutes)

                    val barColor = if (data == 0f) {
                        MnTheme.iconColorScheme.disabled
                    } else if (
                        configure.xAxis[idx].format(parser) == LocalDateTime.now().format(parser)
                    ) {
                        MnTheme.backgroundColorScheme.accent1
                    } else {
                        MnTheme.iconColorScheme.secondary
                    }

                    val height = animateDpAsState(
                        targetValue = if (barAnimated) barHeight else 0.dp,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = idx * 100, // 각 바에 대한 지연 시간
                            easing = FastOutSlowInEasing,
                        ),
                        finishedListener = { barAnimated = true },
                        label = "barHeight",
                    )

                    val color by animateColorAsState(
                        targetValue = if (barAnimated) barColor else Color.Transparent,
                        tween(
                            durationMillis = 2000,
                            delayMillis = idx * 100, // 각 바에 대한 지연 시간
                            easing = FastOutSlowInEasing,
                        ),
                    )

                    val isMaxFocusTime = remember { data > 0 && data == maxFocusTime }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        FocusTimeGraphBar(
                            modifier = Modifier,
                            barColor = color,
                            barHeight = height.value,
                        )

                        Text(
                            modifier = Modifier.absoluteOffset(y = 20.dp),
                            text = configure.xAxis[idx].format(parser),
                            style = MnTheme.typography.captionRegular.copy(fontSize = 11.sp),
                            color = MnTheme.textColorScheme.tertiary,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                        )
                        if (isMaxFocusTime) {
                            MaxFocusTimeToolTip(
                                modifier = Modifier
                                    .absoluteOffset(
                                        x = 0.dp,
                                        y = -(height.value + MnSpacing.xSmall),
                                    )
                                    .wrapContentWidth(unbounded = true)
                                    .padding(bottom = MnSpacing.small),
                                data = graphData[idx],
                                enabled = showTooltip,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusTimeGraphBar(
    modifier: Modifier,
    barColor: Color,
    barHeight: Dp,
) {
    Box(
        modifier = modifier
            .padding(horizontal = MnSpacing.small)
            .background(
                barColor,
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 6.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp,
                ),
            )
            .height(barHeight)
            .fillMaxWidth(),
    )
}

@Composable
private fun MaxFocusTimeToolTip(
    modifier: Modifier,
    data: Float,
    enabled: Boolean,
) {
    AnimatedVisibility(
        visible = enabled,
        enter = fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 200)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 1000),
                initialOffsetY = { it / 3 },
            ),
        exit = fadeOut(animationSpec = tween(durationMillis = 100)) +
            scaleOut(
                animationSpec = tween(durationMillis = 100),
                targetScale = 0.8f,
            ),
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MnTheme.iconColorScheme.primary,
                        shape = RoundedCornerShape(MnRadius.threeXSmall),
                    )
                    .padding(
                        horizontal = MnSpacing.small,
                        vertical = MnSpacing.xSmall,
                    ),
                contentAlignment = Alignment.Center,

            ) {
                val targetData = data.toInt()

                val maxTime = if (targetData > 60) "${(targetData / 60)}시간\n${(targetData % 60)}분" else "${targetData}분"

                Text(
                    text = maxTime,
                    textAlign = TextAlign.Center,
                    color = MnTheme.textColorScheme.inverse,
                    style = MnTheme.typography.captionSemiBold,

                )
            }

            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 8.dp)
                    .align(Alignment.BottomCenter)
                    .absoluteOffset(y = 8.dp)
                    .zIndex(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                FocusTooltipTriangleShape(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MnTheme.iconColorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun GraphData(
    modifier: Modifier,
    configure: FocusGraphConfigure,
    graphData: List<Float>,
    graphDayParser: DateTimeFormatter,
    gap: Int,
    gapHeight: Dp,
) {
    /* 그래프 배경 */
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row {
            /* 점선 */
            Box(modifier = Modifier.weight(1f)) {
                FocusGraphLines(
                    modifier = Modifier.fillMaxSize(),
                    gap = gap,
                    gapHeight = gapHeight,
                )
                FocusGraphXAxis(
                    modifier = Modifier.fillMaxSize(),
                    graphData = graphData,
                    configure = configure,
                    parser = graphDayParser,
                    gapHeight = gapHeight,
                )
            }

            /* Y축 라벨 */
            FocusGraphYLabel(
                gap = gap,
                gapHeight = gapHeight,
                configure = configure,
            )
        }
    }
}

@Composable
private fun FocusGraphYLabel(
    modifier: Modifier = Modifier,
    gap: Int,
    gapHeight: Dp,
    configure: FocusGraphConfigure,
) {
    Box(
        modifier = modifier
            .padding(start = MnSpacing.small)
            .wrapContentHeight(
                align = Alignment.Bottom,
                unbounded = true,
            ),
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
        ) {
            for (i in gap downTo 0) {
                Box(
                    modifier = Modifier
                        .height(gapHeight)
                        .absoluteOffset(
                            y = MnTheme.typography.captionRegular.lineHeight
                                .spToPx().dp /
                                2,
                        ),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    Text(
                        text = configure.getYLabel(i),
                        style = MnTheme.typography.captionRegular.copy(fontSize = 11.sp),
                        color = MnTheme.textColorScheme.tertiary,
                    )
                }
            }
        }
    }
}

@Composable
private fun FocusGraphLines(
    modifier: Modifier = Modifier,
    gap: Int,
    gapHeight: Dp,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        for (i in gap downTo 0) {
            val lineColor = MnTheme.iconColorScheme.disabled
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gapHeight),
            ) {
                if (i > 0) {
                    GraphDashLine(
                        modifier = Modifier.fillMaxWidth(),
                        lineColor = lineColor,
                    )
                } else {
                    Canvas(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        drawLine(
                            color = lineColor,
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = size.width, y = 0f),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusTooltipTriangleShape(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val point1 = Offset(0f, 0f) // 왼쪽 꼭짓점 (top-left)
        val point2 = Offset(size.width, 0f) // 오른쪽 꼭짓점
        val point3 = Offset(size.width / 2f, size.height) // 아래쪽 꼭짓점

        val path = Path().apply {
            moveTo(point1.x, point1.y)
            lineTo(point2.x, point2.y)
            lineTo(point3.x, point3.y)
            close()
        }
        // Draw the triangle path with a blue color
        drawIntoCanvas {
            it.drawOutline(
                outline = Outline.Generic(path),
                paint = Paint().apply {
                    this.color = color
                    pathEffect = PathEffect.cornerPathEffect(1.dp.toPx())
                },
            )
        }
    }
}

@Composable
private fun GraphDashLine(modifier: Modifier, lineColor: Color) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val dashLengthDp = 4.dp
        val gapLengthDp = 4.dp

        val dotPathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLengthDp.toPx(), gapLengthDp.toPx()),
            phase = 0f,
        )

        drawLine(
            color = lineColor,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = canvasWidth, y = 0f),
            pathEffect = dotPathEffect,
            cap = StrokeCap.Round,
            strokeWidth = 1.dp.toPx(),
        )
    }
}

@Preview(showBackground = true, name = "데이터가 없을 때")
@Composable
private fun FocusTimeGraphMax0MinutePreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 10분 이내")
@Composable
private fun FocusTimeGraphMax10MinutePreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(8f, 5f, 0f, 0f, 9f, 2f, 7f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 1시간 이내")
@Composable
private fun FocusTimeGraphMax1HourPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(10f, 10f, 40f, 30f, 9f, 21f, 7f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 1시간 ~ 5시간 이면서 정각")
@Composable
private fun FocusTimeGraphMax5HourPreview2() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(100f, 120f, 20f, 20f, 100f, 21f, 70f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 1시간 ~ 5시간 이내")
@Composable
private fun FocusTimeGraphMax5HourPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(10f, 100f, 190f, 0f, 290f, 222f, 147f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 8시간 이내")
@Composable
private fun FocusTimeGraphMax8HourPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(10f, 100f, 90f, 398f, 290f, 0f, 147f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 20시간 이내")
@Composable
private fun FocusTimeGraphMax20HourPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(200f, 100f, 490f, 398f, 1090f, 0f, 147f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true, name = "최고 집중 시간이 24시간 이내")
@Composable
private fun FocusTimeGraphOver20HourPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf(200f, 400f, 490f, 398f, 1390f, 100f, 547f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FocusTimeGraphPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        val graphData = listOf<Float>(190f, 10f, 320f, 0f, 90f, 120f, 157f).toImmutableList()
        GraphContainer(
            graphData = graphData,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FocusGraphXAxisPreview() {
    val graphData = listOf<Float>(190f, 10f, 320f, 0f, 90f, 120f, 157f).toImmutableList()

    val configure = FocusGraphConfigure(
        maxFocusTime = graphData.max() / 60,
        targetDateTime = LocalDateTime.now(),
    )

    val gap = (configure.maxYAxis.inWholeMinutes / configure.yAxisRange.inWholeMinutes).toFloat()
    val gapHeight = 160.dp / gap

    Box(
        modifier = Modifier.height(400.dp),
        contentAlignment = Alignment.Center,
    ) {
        FocusGraphXAxis(
            modifier = Modifier.height(200.dp),
            graphData = graphData,
            configure = configure,
            gapHeight = gapHeight,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StaticFocusMaxTooltip() {
    Box(modifier = Modifier.size(100.dp)) {
        MaxFocusTimeToolTip(
            modifier = Modifier,
            data = 120f,
            enabled = true,
        )
    }
}
