package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.home.category.component.CategoryBottomSheetHeader
import com.pomonyang.mohanyang.presentation.screen.home.category.component.CategoryManagementControls
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryManageState
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
    var showMoreMenuComponent by rememberSaveable { mutableStateOf(false) }
    var categoryManageState: CategoryManageState by rememberSaveable { mutableStateOf(CategoryManageState.DEFAULT) }
    MnBottomSheet(
        onDismissRequest = { onAction(PomodoroSettingEvent.DismissCategoryDialog) },
        modifier = modifier,
        title = stringResource(R.string.change_category_title),
        headerContents = {
            CategoryBottomSheetHeader(
                onEditClick = {
                    onAction.invoke(PomodoroSettingEvent.ClickCategoryCreate)
                },
                onMoreMenuClick = {
                    showMoreMenuComponent = true
                },
            )
        },
    ) {
        CategoryBottomSheetContents(
            modifier = Modifier.padding(
                start = MnSpacing.large,
                end = MnSpacing.large,
                bottom = MnSpacing.xLarge,
            ),
            categoryList = categoryList,
            showMoreMenuComponent = showMoreMenuComponent,
            currentSelectedCategoryNo = initialCategoryNo,
            onDeleteClick = { categoryManageState = CategoryManageState.DELETE },
            onEditClick = { categoryManageState = CategoryManageState.EDIT },
            onAction = onAction,
        )
    }
}



@Composable
private fun CategoryBottomSheetContents(
    categoryList: ImmutableList<PomodoroCategoryModel>,
    currentSelectedCategoryNo: Int,
    showMoreMenuComponent: Boolean,
    onAction: (PomodoroSettingEvent) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
        ) {
            val gridCells = if (categoryList.size == 1) GridCells.Fixed(1) else GridCells.Fixed(2)

            LazyVerticalGrid(
                columns = gridCells,
                horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
                verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
            ) {
                items(categoryList.size) { index ->
                    val item = categoryList[index]
                    MnSelectListItem(
                        modifier = Modifier.fillMaxWidth(),
                        iconResource = item.categoryType.iconRes,
                        categoryType = item.title,
                        onClick = { onAction(PomodoroSettingEvent.SelectCategory(item.categoryNo)) },
                        isSelected = item.categoryNo == currentSelectedCategoryNo,
                    )
                }
            }
        }

        if (showMoreMenuComponent) {
            CategoryManagementControls(
                modifier = Modifier.align(Alignment.TopEnd),
                onDeleteClick = onDeleteClick,
                onEditClick = onEditClick,
            )
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
            onAction = {},
            showMoreMenuComponent = true,
            onDeleteClick = {},
            onEditClick = {},
        )
    }
}

