package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.screen.home.CategorySetting
import com.pomonyang.mohanyang.presentation.screen.onboarding.naming.ValidationResult
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
                        categoryName = event.categoryName ?: "",
                        selectedCategoryIcon = event.categoryIconId,
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
                updateState { copy(selectedCategoryIcon = event.iconId) }
                setEffect(CategorySettingSideEffect.DismissCategoryIconBottomSheet)
            }

            CategorySettingEvent.DismissBottomSheet -> {
                setEffect(CategorySettingSideEffect.DismissCategoryIconBottomSheet)
            }
        }
    }

    fun validateCategoryName(name: String): ValidationResult {
        // TODO 기존 카테고리 이름 리스트 가져와서 중복값 확인하는 로직 추가

        return ValidationResult(
            isValid = name.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH,
            message = if (name.length <= NAME_MAX_LENGTH) "" else "최대 ${NAME_MAX_LENGTH}자리까지 입력할 수 있어요.",
        )
    }

    companion object {
        private const val NAME_MAX_LENGTH = 10
        private const val NAME_MIN_LENGTH = 1
    }
}
