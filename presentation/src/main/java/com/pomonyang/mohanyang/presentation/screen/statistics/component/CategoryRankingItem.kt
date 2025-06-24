package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.statistics.model.RankingItemModel
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun CategoryRankingItem(
    rankingItemModel: RankingItemModel,
    modifier: Modifier = Modifier,
) {
    val rankingIcon = when (rankingItemModel.rank) {
        1 -> R.drawable.ic_crown_1
        2 -> R.drawable.ic_crown_2
        3 -> R.drawable.ic_crown_3
        else -> R.drawable.ic_null
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = painterResource(id = rankingIcon),
            contentDescription = "랭킹 아이콘",
            modifier = Modifier.size(28.dp),
            tint = Color.Unspecified,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = rankingItemModel.category.title,
            style = MnTheme.typography.bodySemiBold,
            color = MnTheme.textColorScheme.primary,
            maxLines = 1,
        )

        Text(
            text = stringResource(R.string.common_time_format, rankingItemModel.totalFocusTime.inWholeHours, rankingItemModel.totalFocusTime.inWholeMinutes % 60),
            style = MnTheme.typography.bodyRegular,
            color = MnTheme.textColorScheme.tertiary,
        )
    }
}

@ThemePreviews
@Preview
@Composable
private fun PreviewCategoryRankingItem() {
    MnTheme {
        Column {
            CategoryRankingItem(
                rankingItemModel = RankingItemModel(
                    rank = 1,
                    category = PomodoroCategoryModel(
                        title = "운동",
                        categoryNo = 1,
                        categoryIcon = CategoryIcon.SUN,
                    ),
                    totalFocusTime = 140.minutes,
                ),
            )
            CategoryRankingItem(
                rankingItemModel = RankingItemModel(
                    rank = 2,
                    category = PomodoroCategoryModel(
                        title = "공부",
                        categoryNo = 1,
                        categoryIcon = CategoryIcon.SUN,
                    ),
                    totalFocusTime = 100.minutes,
                ),
            )
            CategoryRankingItem(
                rankingItemModel = RankingItemModel(
                    rank = 3,
                    category = PomodoroCategoryModel(
                        title = "독서",
                        categoryNo = 1,
                        categoryIcon = CategoryIcon.SUN,
                    ),
                    totalFocusTime = Duration.ZERO,
                ),
            )
        }
    }
}
