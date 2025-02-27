package com.pomonyang.mohanyang.presentation.screen.home.category

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import kotlinx.collections.immutable.toImmutableList

data class CategorySettingState(
    val categoryNo: Int? = null,
    val categoryName: String = "",
    val selectedCategoryIcon: Int = CategoryIcon.DEFAULT.resourceId,
    val categoryIcons: List<Int> = CategoryIcon.entries.map { it.resourceId }.toImmutableList(),
) : ViewState {
    fun isCreateMode(): Boolean = categoryNo == null
}

sealed interface CategorySettingEvent : ViewEvent {
    data class Init(
        val categoryNo: Int?,
        val categoryName: String,
        val categoryIconId: Int,
    ) : CategorySettingEvent

    data class ClickEdit(val categoryNo: Int?) : CategorySettingEvent
    data class SelectIcon(val iconId: Int) : CategorySettingEvent
    data class ClickConfirm(val categoryName: String) : CategorySettingEvent
    data object DismissBottomSheet : CategorySettingEvent
}

sealed interface CategorySettingSideEffect : ViewSideEffect {
    data object GoToPomodoroSetting : CategorySettingSideEffect
    data object ShowCategoryIconBottomSheet : CategorySettingSideEffect
    data object DismissCategoryIconBottomSheet : CategorySettingSideEffect
}
