package com.pomonyang.mohanyang.presentation.screen.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
fun FocusTimeListItem(
    time: String, // 데이터 형식 어떻게 뽑을지 고민 중
    category: PomodoroCategoryModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnColor.White,
                shape = RoundedCornerShape(MnRadius.small),
            )
            .padding(
                MnSpacing.large,
            ),
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.large),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MnTheme.backgroundColorScheme.primary,
                    shape = RoundedCornerShape(MnRadius.xSmall),
                )
                .padding(
                    MnSpacing.medium,
                ),
            contentAlignment = Alignment.Center,
        ) {
            MnLargeIcon(
                resourceId = category.categoryIcon.resourceId,
                tint = Color.Unspecified,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.twoXSmall),
        ) {
            Text(
                text = category.title,
                style = MnTheme.typography.header5,
                color = MnTheme.textColorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
            Text(
                text = time,
                style = MnTheme.typography.bodyRegular,
                color = MnTheme.textColorScheme.secondary,
            )
        }
    }
}

private class FocusTimeListItemPreviewCollection :
    CollectionPreviewParameterProvider<PomodoroCategoryModel>(
        listOf(
            PomodoroCategoryModel(
                categoryNo = 0,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "열글자까지되는데아악열글자까지되는데아악열글자까지되는데아악열글자까지되는데아악열글자까지되는데아악",
                categoryIcon = CategoryIcon.CAT,
            ),
        ),
    )

@ThemePreviews
@Composable
private fun FocusTimeListItemPreview(
    @PreviewParameter(FocusTimeListItemPreviewCollection::class) category: PomodoroCategoryModel,
) {
    MnTheme {
        FocusTimeListItem(
            category = category,
            time = "1시간 2분 21초",
        )
    }
}
