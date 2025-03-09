package com.pomonyang.mohanyang.presentation.screen.home.category.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryManageState
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryBottomSheetContents(
    categoryList: ImmutableList<PomodoroCategoryModel>,
    categoryManageState: CategoryManageState,
    currentSelectedCategoryNo: Int,
    onCategorySelected: (PomodoroCategoryModel) -> Unit,
    onCategoryEdit: (PomodoroCategoryModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        val gridCells = if (categoryList.size == 1) GridCells.Fixed(1) else GridCells.Fixed(2)

        LazyVerticalGrid(
            columns = gridCells,
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
        ) {
            items(categoryList.size) { index ->
                val item = categoryList[index]
                val isNonEditableItem = index == 0 && categoryManageState.isDefault().not()
                var selected by remember(categoryManageState) { mutableStateOf(item.categoryNo == currentSelectedCategoryNo) }
                MnSelectListItem(
                    modifier = Modifier.fillMaxWidth(),
                    iconResource = if (isNonEditableItem) R.drawable.ic_lock else item.categoryType.iconRes,
                    categoryType = item.title,
                    onClick = {
                        when (categoryManageState) {
                            CategoryManageState.DEFAULT -> onCategorySelected(item)
                            CategoryManageState.EDIT -> onCategoryEdit(item)
                            CategoryManageState.DELETE -> {
                                selected = selected.not()
                            }
                        }
                    },
                    isSelected = if (categoryManageState.isDefault() || categoryManageState.isDelete()) selected else false,
                    isDisabled = isNonEditableItem,
                )
            }
        }
    }
}

private class CategoryPreviewParameterProvider :
    PreviewParameterProvider<List<PomodoroCategoryModel>> {
    override val values = sequenceOf(
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
        ),
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
            PomodoroCategoryModel(
                categoryNo = 2,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
            PomodoroCategoryModel(
                categoryNo = 3,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
        ),
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
            PomodoroCategoryModel(
                categoryNo = 2,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
            PomodoroCategoryModel(
                categoryNo = 3,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
            ),
            PomodoroCategoryModel(
                categoryNo = 4,
                title = "집중",
                categoryType = PomodoroCategoryType.DEFAULT,
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
            categoryManageState = CategoryManageState.EDIT,
            currentSelectedCategoryNo = 1,
            onCategorySelected = {},
            onCategoryEdit = {},
        )
    }
}

