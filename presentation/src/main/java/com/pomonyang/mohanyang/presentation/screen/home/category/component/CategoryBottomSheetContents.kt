package com.pomonyang.mohanyang.presentation.screen.home.category.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryManageState
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun CategoryBottomSheetContents(
    categoryList: ImmutableList<PomodoroCategoryModel>,
    selectedItems: ImmutableMap<Int, Boolean>,
    categoryManageState: CategoryManageState,
    onCategorySelected: (PomodoroCategoryModel) -> Unit,
    onCategoryEdit: (PomodoroCategoryModel) -> Unit,
    onSnackbarShow: (String) -> Unit,
    onDeleteItemClick: () -> Unit,
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridCells = if (categoryList.size == 1) GridCells.Fixed(1) else GridCells.Fixed(2)
    val context = LocalContext.current

    Column(modifier = modifier) {
        LazyVerticalGrid(
            columns = gridCells,
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
        ) {
            items(categoryList.size) { index ->
                val item = categoryList[index]
                val isNonEditableItem = index == 0 && categoryManageState.isDefault().not()
                val selected = selectedItems.getOrElse(item.categoryNo) { false }
                val iconResource = when {
                    isNonEditableItem -> R.drawable.ic_lock
                    categoryManageState.isDelete() -> if (selected) R.drawable.ic_check_circle else R.drawable.ic_circle
                    else -> item.categoryIcon.resourceId
                }
                MnSelectListItem(
                    modifier = Modifier.fillMaxWidth(),
                    iconResource = iconResource,
                    categoryType = item.title,
                    onClick = {
                        when (categoryManageState) {
                            CategoryManageState.DEFAULT -> onCategorySelected(item)
                            CategoryManageState.EDIT -> {
                                if (isNonEditableItem.not()) {
                                    onCategoryEdit(item)
                                } else {
                                    onSnackbarShow(context.getString(R.string.default_category_cannot_edit))
                                }
                            }

                            CategoryManageState.DELETE -> {
                                if (isNonEditableItem.not()) {
                                    onSelectItem(item.categoryNo)
                                } else {
                                    onSnackbarShow(context.getString(R.string.default_category_cannot_edit))
                                }
                            }
                        }
                    },
                    isSelected = if (categoryManageState.isDefault() || categoryManageState.isDelete()) selected else false,
                    isDisabled = isNonEditableItem,
                )
            }
        }

        if (categoryManageState.isDelete()) {
            val isEnabled = selectedItems.values.count { it } != 0
            val buttonColor = if (isEnabled) MnBoxButtonColorType.secondary else MnBoxButtonColorType.primary
            MnBoxButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.change_category_delete_count, selectedItems.values.count { it }),
                onClick = onDeleteItemClick,
                colors = buttonColor,
                isEnabled = isEnabled,
                styles = MnBoxButtonStyles.large,
                containerPadding = PaddingValues(top = 23.dp),
            )
        }
    }
}

private class CategoryPreviewParameterProvider : PreviewParameterProvider<List<PomodoroCategoryModel>> {
    override val values = sequenceOf(
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
        ),
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
            PomodoroCategoryModel(
                categoryNo = 2,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
            PomodoroCategoryModel(
                categoryNo = 3,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
        ),
        persistentListOf(
            PomodoroCategoryModel(
                categoryNo = 1,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
            PomodoroCategoryModel(
                categoryNo = 2,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
            PomodoroCategoryModel(
                categoryNo = 3,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
            ),
            PomodoroCategoryModel(
                categoryNo = 4,
                title = "집중",
                categoryIcon = CategoryIcon.CAT,
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
            selectedItems = persistentMapOf(),
            onDeleteItemClick = {},
            onSelectItem = {},
            onCategorySelected = {},
            onCategoryEdit = {},
            onSnackbarShow = {},
        )
    }
}
