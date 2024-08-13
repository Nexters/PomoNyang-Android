@file:OptIn(ExperimentalFoundationApi::class)

package com.pomonyang.mohanyang.presentation.designsystem.picker

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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MnWheelMinutePicker(
    items: PersistentList<Int>,
    initialItem: Int,
    onChangePickTime: (Int) -> Unit,
    modifier: Modifier = Modifier,
    mnWheelPickerColor: MnWheelPickerColor = MnWheelPickerDefaults.colors(),
    mnWheelPickerStyles: MnWheelPickerStyles = MnWheelPickerDefaults.styles()
) {
    val screenHeightPixel = LocalConfiguration.current.screenHeightDp
    val halfDisplayCount = when {
        screenHeightPixel <= 700 -> 1
        screenHeightPixel in 701..950 -> 2
        screenHeightPixel in 950..1150 -> 3
        else -> 4
    }
    val scrollState = rememberLazyListState(items.indexOf(initialItem))
    val coroutineScope = rememberCoroutineScope()
    val itemHeight = MnWheelPickerDefaults.itemHeight
    val focusItemHeight = MnWheelPickerDefaults.focusItemHeight
    val wheelHeight = (2 * halfDisplayCount) * (itemHeight + MnWheelPickerDefaults.itemSpacing) + focusItemHeight
    val brushColor = mnWheelPickerColor.fadeColor

    LaunchedEffect(Unit) {
        delay(100)
        scrollState.animateScrollToItem(items.indexOf(initialItem))
    }

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
            selectedTextStyle = mnWheelPickerStyles.selectedTextStyle,
            unSelectedTextStyle = mnWheelPickerStyles.unSelectedTextStyle,
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
            .width(209.dp)
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
    selectedTextStyle: TextStyle,
    unSelectedTextStyle: TextStyle,
    selectedTextColor: Color,
    unSelectedTextColor: Color,
    onChangePickTime: (item: Int) -> Unit,
    onItemClick: (index: Int) -> Unit,
    onScrollEnd: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                scrollState.getCentralItemIndex()?.let(onScrollEnd)
                return super.onPostFling(consumed, available)
            }
        }
    }
    LazyColumn(
        modifier = modifier
            .matchParentSize()
            .zIndex(1f)
            .nestedScroll(nestedScrollConnection),
        contentPadding = PaddingValues(vertical = (itemHeight + MnWheelPickerDefaults.itemSpacing) * halfDisplayCount),
        verticalArrangement = Arrangement.spacedBy(MnWheelPickerDefaults.itemSpacing),
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

            Box(
                modifier = Modifier
                    .height(if (isCentralItem) focusItemHeight else itemHeight)
                    .padding(start = if (isCentralItem) 36.dp else 0.dp)
                    .fillMaxWidth()
                    .noRippleClickable { onItemClick(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$item:00",
                    style = if (isCentralItem) selectedTextStyle else unSelectedTextStyle,
                    color = if (isCentralItem) selectedTextColor else unSelectedTextColor
                )
            }
        }
    }
}

private fun LazyListState.getCentralItemIndex(): Int? {
    val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    val centralItemInfo = layoutInfo.visibleItemsInfo.find { itemInfo ->
        val itemCenter = (itemInfo.offset + itemInfo.size / 2)
        itemCenter in (viewportCenter - itemInfo.size / 2)..(viewportCenter + itemInfo.size / 2)
    }
    return centralItemInfo?.index
}

@Preview
@Composable
private fun MnWheelPickerPreview() {
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
