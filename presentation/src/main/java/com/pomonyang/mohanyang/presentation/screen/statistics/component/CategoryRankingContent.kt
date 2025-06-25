package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.statistics.model.RankingItemModel
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import java.time.Duration
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryRankingContent(
    startDate: LocalDate,
    endDate: LocalDate,
    categoryRankingList: ImmutableList<RankingItemModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MnSpacing.xLarge)
            .padding(bottom = MnSpacing.xLarge),
    ) {
        RankingTitle(startDate, endDate)
        RankingContent(categoryRankingList)
    }
}

@Composable
private fun RankingTitle(
    startDate: LocalDate,
    endDate: LocalDate,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = MnSpacing.xLarge),
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.total_focus_category_ranking),
            style = MnTheme.typography.header4,
            color = MnTheme.textColorScheme.primary,
        )
        Text(
            text = "${
                stringResource(
                    R.string.common_day_format,
                    startDate.monthValue,
                    startDate.dayOfMonth,
                )
            } - ${
                stringResource(
                    R.string.common_day_format,
                    endDate.monthValue,
                    endDate.dayOfMonth,
                )
            }",
            style = MnTheme.typography.subBodyRegular,
            color = MnTheme.textColorScheme.tertiary,
        )
    }
}

@Composable
private fun RankingContent(
    categoryRankingList: ImmutableList<RankingItemModel>,
    modifier: Modifier = Modifier,
) {
    if (categoryRankingList.isEmpty()) {
        TotalFocusTimeContent(
            focusTime = Duration.ZERO,
        )
    } else {
        Column(
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(MnRadius.small),
                    color = MnTheme.iconColorScheme.inverse,
                )
                .padding(MnSpacing.large),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.large),
        ) {
            categoryRankingList.forEachIndexed { idx, item ->
                key(idx) {
                    CategoryRankingItem(
                        rankingItemModel = item,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

private class CategoryRankingListProvider : PreviewParameterProvider<ImmutableList<RankingItemModel>> {
    override val values = sequenceOf(
        persistentListOf(),
        persistentListOf(
            RankingItemModel(
                rank = 1,
                category = PomodoroCategoryModel(
                    categoryNo = 0,
                    title = "집중",
                    categoryIcon = CategoryIcon.CAT,
                ),
                totalFocusTime = Duration.ZERO,
            ),
            RankingItemModel(
                rank = 2,
                category = PomodoroCategoryModel(
                    categoryNo = 0,
                    title = "공부",
                    categoryIcon = CategoryIcon.ASTERISK,
                ),
                totalFocusTime = Duration.ZERO,
            ),
            RankingItemModel(
                rank = 3,
                category = PomodoroCategoryModel(
                    categoryNo = 0,
                    title = "놀기",
                    categoryIcon = CategoryIcon.HEART,
                ),
                totalFocusTime = Duration.ZERO,
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryRankingContentPreview(
    @PreviewParameter(CategoryRankingListProvider::class) categoryRankingList: ImmutableList<RankingItemModel>,
) {
    MnTheme {
        CategoryRankingContent(
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            categoryRankingList = categoryRankingList,
        )
    }
}
