package com.pomonyang.mohanyang.presentation.screen.statistics.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.statistics.component.Dot
import com.pomonyang.mohanyang.presentation.screen.statistics.component.FocusTimeListItem
import com.pomonyang.mohanyang.presentation.screen.statistics.component.StatisticsContentHeader
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun StatisticsContent(
    time: String,
    category: PomodoroCategoryModel,
    modifier: Modifier = Modifier,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val dotHorizontalPadding = 9.5.dp.roundToPx()
        val dotHeight = 5.dp.roundToPx()
        val dotWidth = 1.dp.roundToPx()
        val dotSpacing = 3.dp.roundToPx()

        val focusPlaceable = subcompose("focusTimeListItem") {
            FocusTimeListItem(
                time = time,
                category = category,
                modifier = Modifier.padding(vertical = MnSpacing.small),
            )
        }.map { measurables ->
            measurables
                .measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth - (dotWidth + dotHorizontalPadding * 2),
                    ),
                )
        }.first()

        val slotWithSpacing = dotHeight + dotSpacing
        val totalDotCount = ((focusPlaceable.height + dotSpacing) / slotWithSpacing).coerceAtLeast(1)
        val dotPlaceables = subcompose("dots") {
            repeat(totalDotCount) { Dot() }
        }.map { measurable ->
            measurable.measure(
                constraints.copy(
                    minWidth = dotWidth,
                    maxWidth = dotWidth,
                    minHeight = dotHeight,
                    maxHeight = dotHeight,
                ),
            )
        }

        val totalDotsHeight =
            dotHeight * totalDotCount + dotSpacing * (totalDotCount - 1)
        val startY = (focusPlaceable.height - totalDotsHeight) / 2
        val totalWidth = focusPlaceable.width + 4.dp.roundToPx() + dotWidth
        val totalHeight = focusPlaceable.height

        layout(totalWidth, totalHeight) {
            dotPlaceables.forEachIndexed { index, placeable ->
                val y = startY + index * (dotHeight + dotSpacing)
                placeable.place(
                    x = dotHorizontalPadding,
                    y = y,
                )
            }
            focusPlaceable.place(
                x = dotWidth + dotHorizontalPadding * 2 + 4.dp.roundToPx(),
                y = 0,
            )
        }
    }
}

@Preview
@Composable
private fun StatisticsContentPreview() {
    MnTheme {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            StatisticsContentHeader(
                time = "11:58-13:32",
            )
            StatisticsContent(
                time = "1시간 2분 21초",
                category = PomodoroCategoryModel(
                    categoryNo = 0,
                    title = "집중",
                    categoryIcon = CategoryIcon.CAT,
                ),
            )
            StatisticsContentHeader(
                time = "11:58-13:32",
            )
            StatisticsContent(
                time = "1시간 2분 21초",
                category = PomodoroCategoryModel(
                    categoryNo = 0,
                    title = "집중",
                    categoryIcon = CategoryIcon.CAT,
                ),
            )
        }
    }
}
