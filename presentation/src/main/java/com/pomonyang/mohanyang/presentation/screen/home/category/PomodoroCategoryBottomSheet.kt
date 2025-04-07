package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog
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
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch

@Composable
fun PomodoroCategoryBottomSheet(
    onAction: (PomodoroSettingEvent) -> Unit,
    categoryList: ImmutableList<PomodoroCategoryModel>,
    initialCategoryNo: Int,
    bottomSheetSnackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    var showMoreMenuComponent by rememberSaveable { mutableStateOf(false) }
    var categoryManageState: CategoryManageState by remember(categoryList) { mutableStateOf(CategoryManageState.DEFAULT) }

    PomodoroCategoryBottomSheet(
        categoryManageState = categoryManageState,
        showMoreMenuComponent = showMoreMenuComponent,
        categoryList = categoryList,
        initialCategoryNo = initialCategoryNo,
        snackbarHostState = bottomSheetSnackbarHostState,
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
    snackbarHostState: SnackbarHostState,
    onAction: (PomodoroSettingEvent) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onMoreMenuClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedItems: MutableMap<Int, Boolean> = remember(categoryList) { mutableStateMapOf<Int, Boolean>() }
    val subTitle = if (categoryManageState.isEdit()) stringResource(R.string.change_category_edit_sub_title) else null
    val scope = rememberCoroutineScope()

    var showDeleteAlertDialog by remember { mutableStateOf(false) }

    if (showDeleteAlertDialog) {
        MnDialog(
            title = stringResource(R.string.dialog_delete_category_title),
            subTitle = stringResource(R.string.dialog_delete_category_subtitle),
            positiveButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.common_cancel),
                    onClick = { showDeleteAlertDialog = false },
                    colors = MnBoxButtonColorType.tertiary,
                    styles = MnBoxButtonStyles.medium,
                )
            },
            negativeButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.delete_category),
                    onClick = {
                        showDeleteAlertDialog = false
                        onAction(PomodoroSettingEvent.DeleteCategories(selectedItems.map { it.key }))
                    },
                    colors = MnBoxButtonColorType.secondary,
                    styles = MnBoxButtonStyles.medium,
                )
            },
            onDismissRequest = {
                showDeleteAlertDialog = false
            },
        )
    }

    LaunchedEffect(categoryManageState) {
        selectedItems.clear()
        if (categoryManageState.isDefault()) {
            selectedItems[initialCategoryNo] = true
        }
    }

    MnBottomSheet(
        onDismissRequest = { onAction(PomodoroSettingEvent.DismissCategoryDialog) },
        modifier = modifier,
        title = stringResource(categoryManageState.title),
        subTitle = subTitle,
        headerContents = {
            if (categoryManageState.isDefault()) {
                CategoryBottomSheetHeaderContents(
                    isVisibleAddButton = categoryList.size != 10,
                    onEditClick = {
                        onAction.invoke(PomodoroSettingEvent.ClickCategoryCreate)
                    },
                    onMoreMenuClick = onMoreMenuClick,
                )
            } else {
                MnTextButton(
                    text = stringResource(R.string.common_cancel),
                    styles = MnTextButtonStyles.medium,
                    onClick = onCancelClick,
                )
            }
        },
    ) {
        Box {
            CategoryBottomSheetContents(
                selectedItems = selectedItems.toImmutableMap(),
                categoryManageState = categoryManageState,
                categoryList = categoryList,
                onCategorySelected = { onAction(PomodoroSettingEvent.SelectCategory(it.categoryNo)) },
                onCategoryEdit = { onAction(PomodoroSettingEvent.ClickCategoryEdit(it)) },
                onSnackbarShow = { scope.launch { snackbarHostState.showSnackbar(it) } },
                onDeleteItemClick = { showDeleteAlertDialog = true },
                onSelectItem = { selectedItems[it] = selectedItems[it]?.not() ?: false },
                modifier = Modifier.padding(MnSpacing.xLarge),
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

private class CategoryStatePreviewParameterProvider :
    CollectionPreviewParameterProvider<CategoryManageState>(
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
            snackbarHostState = SnackbarHostState(),
            initialCategoryNo = 1,
            onAction = { },
            onDeleteClick = { },
            onEditClick = { },
            onMoreMenuClick = { },
            onCancelClick = { },
        )
    }
}
