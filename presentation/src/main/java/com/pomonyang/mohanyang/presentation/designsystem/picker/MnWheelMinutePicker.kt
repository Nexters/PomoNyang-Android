@file:OptIn(ExperimentalFoundationApi::class)

package com.pomonyang.mohanyang.presentation.designsystem.picker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.dpToPx
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun MnWheelMinutePicker(
    items: PersistentList<Int>,
    initialItem: Int,
    onChangePickTime: (Int) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MnTheme.typography.header2,
    mnWheelPickerColor: MnWheelPickerColor = MnWheelPickerDefaults.colors()
) {
    val screenHeightPixel = LocalConfiguration.current.screenHeightDp.dp.dpToPx()
    val halfDisplayCount = when {
        screenHeightPixel <= 640f -> 1
        screenHeightPixel in 641f..740f -> 2
        screenHeightPixel in 741f..812f -> 3
        else -> 4
    }
    val scrollState = rememberLazyListState(items.indexOf(initialItem))
    val coroutineScope = rememberCoroutineScope()
    val itemHeight = MnWheelPickerDefaults.itemHeight
    val focusItemHeight = MnWheelPickerDefaults.focusItemHeight
    val wheelHeight = (2 * halfDisplayCount) * itemHeight + focusItemHeight
    val brushColor = mnWheelPickerColor.fadeColor

    Box(
        modifier = modifier
            .height(wheelHeight)
            .fillMaxWidth()
            .fadeEdge(brushColor)
    ) {
        CenterHighlightBox(
            itemHeight = focusItemHeight,
            highlightColor = MnTheme.backgroundColorScheme.secondary
        )

        WheelItemList(
            items = items,
            scrollState = scrollState,
            itemHeight = itemHeight,
            focusItemHeight = focusItemHeight,
            halfDisplayCount = halfDisplayCount,
            textStyle = textStyle,
            selectedTextColor = mnWheelPickerColor.selectedTextColor,
            unSelectedTextColor = mnWheelPickerColor.unSelectedTextColor,
            onItemClick = { index ->
                coroutineScope.launch {
                    scrollState.scrollToItem(index)
                }
            },
            onChangePickTime = { item ->
                onChangePickTime(item)
            },
            onScrollEnd = { index ->
                coroutineScope.launch {
                    scrollState.animateScrollToItem(index)
                }
            }
        )
    }
}

@Composable
private fun Modifier.fadeEdge(color: Color): Modifier {
    val fadeEdgeBrushColor = remember {
        arrayOf(
            0f to color.copy(alpha = 0.95f),
            0.25f to color.copy(alpha = 0.3f),
            0.5f to Color.Transparent,
            0.75f to color.copy(alpha = 0.3f),
            1f to color.copy(alpha = 0.95f)
        )
    }
    return this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithCache {
            val brush = Brush.verticalGradient(
                colorStops = fadeEdgeBrushColor,
                startY = 0f,
                endY = size.height
            )
            onDrawWithContent {
                drawContent()
                drawRect(
                    brush = brush,
                    blendMode = BlendMode.DstOut
                )
            }
        }
}

@Composable
private fun BoxScope.CenterHighlightBox(
    itemHeight: Dp,
    highlightColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(200.dp)
            .height(itemHeight)
            .align(Alignment.Center)
            .background(highlightColor, RoundedCornerShape(MnRadius.medium)),
        contentAlignment = Alignment.CenterStart
    ) {
        MnMediumIcon(resourceId = R.drawable.ic_null, modifier = Modifier.padding(start = 20.dp))
    }
}

@Composable
private fun BoxScope.WheelItemList(
    items: PersistentList<Int>,
    scrollState: LazyListState,
    itemHeight: Dp,
    focusItemHeight: Dp,
    halfDisplayCount: Int,
    textStyle: TextStyle,
    selectedTextColor: Color,
    unSelectedTextColor: Color,
    onChangePickTime: (item: Int) -> Unit,
    onItemClick: (index: Int) -> Unit,
    onScrollEnd: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isScrollInProgress by remember {
        derivedStateOf { scrollState.isScrollInProgress }
    }

    LaunchedEffect(isScrollInProgress) {
        if (isScrollInProgress.not()) {
            val centralItemIndex = scrollState.getCentralItemIndex() ?: 0
            if (scrollState.firstVisibleItemIndex != centralItemIndex) {
                onScrollEnd(centralItemIndex)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .matchParentSize()
            .zIndex(1f),
        contentPadding = PaddingValues(vertical = itemHeight * halfDisplayCount),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(scrollState)
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.toString() }
        ) { index, item ->
            val isCentralItem by remember {
                derivedStateOf {
                    scrollState.getCentralItemIndex() == index
                }
            }

            if (isCentralItem) {
                onChangePickTime(item)
            }

            val scale by animateFloatAsState(
                targetValue = if (isCentralItem) 1.2f else 1f,
                animationSpec = tween(durationMillis = 100),
                label = "scaleAnimation"
            )

            Box(
                modifier = Modifier
                    .height(if (isCentralItem) focusItemHeight else itemHeight)
                    .padding(start = 24.dp)
                    .fillMaxWidth()
                    .scale(scale)
                    .noRippleClickable { onItemClick(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$item:00",
                    style = textStyle,
                    color = if (isCentralItem) selectedTextColor else unSelectedTextColor
                )
            }
        }
    }
}

private fun LazyListState.getCentralItemIndex(): Int? {
    val layoutInfo = layoutInfo
    val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    val centralItemInfo = layoutInfo.visibleItemsInfo.find { itemInfo ->
        val itemCenter = (itemInfo.offset + itemInfo.size / 2)
        itemCenter in (viewportCenter - itemInfo.size / 2)..(viewportCenter + itemInfo.size / 2)
    }
    return centralItemInfo?.index
}

@Preview
@Composable
fun MnWheelPickerPreview() {
    MnTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MnTheme.backgroundColorScheme.primary)
        )
        MnWheelMinutePicker(
            items = (5..60 step 5).toPersistentList(),
            onChangePickTime = {},
            initialItem = 25
        )
    }
}
