package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.screen.home.CategorySetting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategorySettingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<CategorySettingState, CategorySettingEvent, CategorySettingSideEffect>() {
    init {
        val category = savedStateHandle.toRoute<CategorySetting>()
        handleEvent(
            CategorySettingEvent.Init(
                categoryNo = category.categoryNo,
                categoryName = category.categoryName,
                categoryIconId = category.categoryIconId,
            ),
        )
    }

    override fun setInitialState(): CategorySettingState = CategorySettingState()

    override fun handleEvent(event: CategorySettingEvent) {
        when (event) {
            is CategorySettingEvent.Init -> {
                updateState {
                    copy(
                        categoryNo = event.categoryNo,
                        categoryName = event.categoryName,
                        selectedCategoryIconId = event.categoryIconId,
                    )
                }
            }

            is CategorySettingEvent.ClickEdit -> {
                setEffect(CategorySettingSideEffect.ShowCategoryIconBottomSheet)
            }

            is CategorySettingEvent.ClickConfirm -> {
                if (state.value.isCreateMode()) {
                    // TODO 카테고리 생성 API
                } else {
                    // TODO 카테고리 수정 API
                }
                setEffect(CategorySettingSideEffect.GoToPomodoroSetting)
            }

            is CategorySettingEvent.SelectIcon -> {
                updateState { copy(selectedCategoryIconId = event.iconId) }
                setEffect(CategorySettingSideEffect.DismissCategoryIconBottomSheet)
            }

            CategorySettingEvent.DismissBottomSheet -> {
                setEffect(CategorySettingSideEffect.DismissCategoryIconBottomSheet)
            }
        }
    }
}
