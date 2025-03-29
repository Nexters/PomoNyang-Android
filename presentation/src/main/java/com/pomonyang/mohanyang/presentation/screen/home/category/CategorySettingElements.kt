package com.pomonyang.mohanyang.presentation.screen.home.category

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CategorySettingState(
    val categoryNo: Int? = null,
    val categoryName: String = "",
    val selectedCategoryIcon: CategoryIcon = CategoryIcon.CAT,
    val categoryIcons: ImmutableList<CategoryIcon> = CategoryIcon.entries.toImmutableList(),
) : ViewState {
    fun isCreateMode(): Boolean = categoryNo == null
}

sealed interface CategorySettingEvent : ViewEvent {
    data class Init(
        val categoryNo: Int?,
        val categoryName: String,
        val categoryIcon: CategoryIcon,
    ) : CategorySettingEvent

    data class ClickEdit(val categoryNo: Int?) : CategorySettingEvent
    data class SelectIcon(val icon: CategoryIcon) : CategorySettingEvent
    data class ClickConfirm(val categoryName: String) : CategorySettingEvent
    data object DismissBottomSheet : CategorySettingEvent
}

sealed interface CategorySettingSideEffect : ViewSideEffect {
    data object GoToPomodoroSetting : CategorySettingSideEffect
    data object ShowCategoryIconBottomSheet : CategorySettingSideEffect
    data object DismissCategoryIconBottomSheet : CategorySettingSideEffect
    data class ShowErrorMessage(val message: String) : CategorySettingSideEffect
}
