package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.home.setting.PomodoroSettingEvent
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PomodoroCategoryBottomSheet(
    onAction: (PomodoroSettingEvent) -> Unit,
    categoryList: ImmutableList<PomodoroCategoryModel>,
    initialCategoryNo: Int,
    modifier: Modifier = Modifier,
) {
    var currentSelectedCategoryNo by remember { mutableIntStateOf(initialCategoryNo) }

    MnBottomSheet(
        onDismissRequest = { onAction(PomodoroSettingEvent.DismissCategoryDialog) },
        modifier = modifier,
        title = stringResource(R.string.change_category_title),
    ) {
        CategoryBottomSheetContents(
            modifier = Modifier.padding(
                start = MnSpacing.large,
                end = MnSpacing.large,
                top = MnSpacing.xLarge,
                bottom = 36.dp,
            ),
            categoryList = categoryList,
            currentSelectedCategoryNo = currentSelectedCategoryNo,
            onCategoryClick = { currentSelectedCategoryNo = it },
            onAction = onAction,
        )
    }
}

@Composable
private fun CategoryBottomSheetContents(
    categoryList: ImmutableList<PomodoroCategoryModel>,
    currentSelectedCategoryNo: Int,
    onCategoryClick: (Int) -> Unit,
    onAction: (PomodoroSettingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        val gridCells = if (categoryList.size == 1) GridCells.Fixed(1) else GridCells.Fixed(2)

        LazyVerticalGrid(
            columns = gridCells,
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.small)
        ) {
            items(categoryList.size) { index ->
                val item = categoryList[index]
                MnSelectListItem(
                    modifier = Modifier.fillMaxWidth(),
                    iconResource = item.categoryType.iconRes,
                    categoryType = item.title,
                    onClick = { onCategoryClick(item.categoryNo) },
                    isSelected = item.categoryNo == currentSelectedCategoryNo,
                )
            }
        }
    }
}

private class CategoryPreviewParameterProvider : PreviewParameterProvider<List<PomodoroCategoryModel>> {
    override val values = sequenceOf(
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
        ),
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
            PomodoroCategoryModel(
                categoryNo = 2,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
            PomodoroCategoryModel(
                categoryNo = 3,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
        ),
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
            PomodoroCategoryModel(
                categoryNo = 2,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
            PomodoroCategoryModel(
                categoryNo = 3,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
            PomodoroCategoryModel(
                categoryNo = 4,
                title = "집중",
                categoryType = PomodoroCategoryType.WORK,
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryBottomSheetPreview(
    @PreviewParameter(CategoryPreviewParameterProvider::class) categoryList: ImmutableList<PomodoroCategoryModel>,
) {
    MnTheme {
        CategoryBottomSheetContents(
            categoryList = categoryList,
            currentSelectedCategoryNo = 1,
            onCategoryClick = {},
            onAction = {},
        )
    }
}
