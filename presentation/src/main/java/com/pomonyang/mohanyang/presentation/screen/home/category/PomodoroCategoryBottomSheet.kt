package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
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
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.change_category_title),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            categoryList.forEach { pomodoroCategoryModel ->
                MnSelectListItem(
                    containerPadding = PaddingValues(bottom = MnSpacing.small),
                    iconResource = pomodoroCategoryModel.categoryType.iconRes,
                    categoryType = pomodoroCategoryModel.title,
                    onClick = { currentSelectedCategoryNo = pomodoroCategoryModel.categoryNo },
                    isSelected = pomodoroCategoryModel.categoryNo == currentSelectedCategoryNo,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            MnBoxButton(
                containerPadding = PaddingValues(top = MnSpacing.xSmall, bottom = MnSpacing.medium),
                modifier = Modifier.fillMaxWidth(),
                styles = MnBoxButtonStyles.large,
                text = stringResource(R.string.confirm),
                onClick = {
                    onAction(
                        PomodoroSettingEvent.ClickCategoryConfirmButton(currentSelectedCategoryNo),
                    )
                },
                colors = MnBoxButtonColorType.secondary,
            )
        }
    }
}

@Preview
@Composable
private fun PomodoroCategoryBottomSheetPreview() {
    MnTheme {
        PomodoroCategoryBottomSheet(
            onAction = {},
            categoryList = persistentListOf(
                PomodoroCategoryModel(
                    categoryNo = 1,
                    title = "Pomodoro",
                    categoryType = PomodoroCategoryType.WORK,
                ),
                PomodoroCategoryModel(
                    categoryNo = 1,
                    title = "Pomodoro",
                    categoryType = PomodoroCategoryType.WORK,
                ),
                PomodoroCategoryModel(
                    categoryNo = 1,
                    title = "Pomodoro",
                    categoryType = PomodoroCategoryType.WORK,
                ),
            ),
            initialCategoryNo = 1,
        )
    }
}
