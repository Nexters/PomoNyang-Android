package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.toast.MnToastSnackbarHost
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.component.CategoryActionMoreMenuList
import com.pomonyang.mohanyang.presentation.screen.home.category.component.CategoryBottomSheetContents
import com.pomonyang.mohanyang.presentation.screen.home.category.component.CategoryBottomSheetHeaderContents
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryManageState
import com.pomonyang.mohanyang.presentation.screen.home.setting.PomodoroSettingEvent
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun PomodoroCategoryBottomSheet(
    onAction: (PomodoroSettingEvent) -> Unit,
    categoryList: ImmutableList<PomodoroCategoryModel>,
    initialCategoryNo: Int,
    modifier: Modifier = Modifier,
) {
    var showMoreMenuComponent by rememberSaveable { mutableStateOf(false) }
    var categoryManageState: CategoryManageState by remember { mutableStateOf(CategoryManageState.DEFAULT) }
    PomodoroCategoryBottomSheet(
        categoryManageState = categoryManageState,
        showMoreMenuComponent = showMoreMenuComponent,
        categoryList = categoryList,
        initialCategoryNo = initialCategoryNo,
        onAction = onAction,
        onDeleteClick = {
            showMoreMenuComponent = false
            categoryManageState = CategoryManageState.DELETE
        },
        onEditClick = {
            showMoreMenuComponent = false
            categoryManageState = CategoryManageState.EDIT
        },
        onMoreMenuClick = { showMoreMenuComponent = showMoreMenuComponent.not() },
        onCancelClick = { categoryManageState = CategoryManageState.DEFAULT },
        modifier = modifier,
    )
}

@Composable
private fun PomodoroCategoryBottomSheet(
    categoryManageState: CategoryManageState,
    showMoreMenuComponent: Boolean,
    categoryList: ImmutableList<PomodoroCategoryModel>,
    initialCategoryNo: Int,
    onAction: (PomodoroSettingEvent) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onMoreMenuClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subTitle = if (categoryManageState.isEdit()) stringResource(R.string.change_category_edit_sub_title) else null
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    MnBottomSheet(
        onDismissRequest = { onAction(PomodoroSettingEvent.DismissCategoryDialog) },
        modifier = modifier,
        title = stringResource(categoryManageState.title),
        subTitle = subTitle,
        headerContents = {
            if (categoryManageState.isDefault()) {
                CategoryBottomSheetHeaderContents(
                    onEditClick = {
                        onAction.invoke(PomodoroSettingEvent.ClickCategoryCreate)
                    },
                    onMoreMenuClick = onMoreMenuClick,
                )
            } else {
                MnTextButton(
                    text = stringResource(R.string.change_category_title_content),
                    styles = MnTextButtonStyles.medium,
                    onClick = onCancelClick,
                )
            }
        },
    ) {
        Box {
            CategoryBottomSheetContents(
                modifier = Modifier.padding(MnSpacing.xLarge),
                categoryManageState = categoryManageState,
                categoryList = categoryList,
                currentSelectedCategoryNo = initialCategoryNo,
                onCategorySelected = { onAction(PomodoroSettingEvent.SelectCategory(it.categoryNo)) },
                onCategoryEdit = {
                    if (it.categoryIcon == CategoryIcon.CAT) {
                        scope.launch {
                            snackbarHostState.showSnackbar("기본 카테고리는 수정/삭제가 불가능합니다.")
                        }
                    } else {
                        onAction(PomodoroSettingEvent.ClickCategoryEdit(it))
                    }
                },
            )

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopEnd),
                visible = showMoreMenuComponent,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CategoryActionMoreMenuList(
                    onDeleteClick = onDeleteClick,
                    onEditClick = onEditClick,
                )
            }

            MnToastSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

private class CategoryStatePreviewParameterProvider : CollectionPreviewParameterProvider<CategoryManageState>(
    CategoryManageState.entries,
)

@Preview(showBackground = true)
@Composable
private fun CategoryBottomSheetPreview(
    @PreviewParameter(CategoryStatePreviewParameterProvider::class) categoryManageState: CategoryManageState,
) {
    MnTheme {
        PomodoroCategoryBottomSheet(
            categoryManageState = categoryManageState,
            showMoreMenuComponent = true,
            categoryList = persistentListOf(
                PomodoroCategoryModel(
                    categoryNo = 1,
                    title = "집중",
                    categoryIcon = CategoryIcon.CAT,
                ),
            ),
            initialCategoryNo = 1,
            onAction = { },
            onDeleteClick = { },
            onEditClick = { },
            onMoreMenuClick = { },
            onCancelClick = { },
        )
    }
}

