package com.pomonyang.mohanyang.presentation.screen.home.setting

import androidx.annotation.DrawableRes
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroSettingModel

data class PomodoroSettingState(
    val categoryList: List<PomodoroCategoryModel> = emptyList(),
    val selectedSettingModel: PomodoroSettingModel = PomodoroSettingModel(
        categoryNo = 0,
        title = "집중",
        categoryType = PomodoroCategoryType.DEFAULT,
        focusTime = 0,
        restTime = 0,
    ),
    val showCategoryBottomSheet: Boolean = false,
    val cat: CatInfoModel = CatInfoModel(
        no = -1,
        name = "",
        type = CatType.CHEESE,
    ),
    val isEndOnBoardingTooltip: Boolean = false,
) : ViewState

sealed interface PomodoroSettingEvent : ViewEvent {
    data object Init : PomodoroSettingEvent
    data object ClickCategory : PomodoroSettingEvent
    data object ClickRestTime : PomodoroSettingEvent
    data object ClickFocusTime : PomodoroSettingEvent
    data object ClickStartPomodoroSetting : PomodoroSettingEvent
    data object DismissCategoryDialog : PomodoroSettingEvent
    data object DismissOnBoardingTooltip : PomodoroSettingEvent
    data class SelectCategory(val categoryNo: Int) : PomodoroSettingEvent
    data object ClickMenu : PomodoroSettingEvent
    data class ClickCategoryEdit(val category: PomodoroCategoryModel) : PomodoroSettingEvent
    data object ClickCategoryCreate : PomodoroSettingEvent
    data class ClickCategoryDelete(val category: PomodoroCategoryModel) : PomodoroSettingEvent
}

sealed interface PomodoroSettingSideEffect : ViewSideEffect {
    data object GoToPomodoro : PomodoroSettingSideEffect

    data class ShowSnackBar(
        val message: String,
        @DrawableRes val iconRes: Int,
    ) : PomodoroSettingSideEffect

    data class GoTimeSetting(
        val isFocusTime: Boolean,
        val initialTime: Int,
        val category: String,
    ) : PomodoroSettingSideEffect

    data object GoToMyPage : PomodoroSettingSideEffect

    data class GoToCategoryEdit(val category: PomodoroCategoryModel) : PomodoroSettingSideEffect

    data object GoToCategoryCreate : PomodoroSettingSideEffect
}
