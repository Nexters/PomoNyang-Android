package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.screen.home.CategorySetting
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon.CategoryIconNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.coroutines.launch

@HiltViewModel
class CategorySettingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pomodoroSettingRepository: PomodoroSettingRepository,
) : BaseViewModel<CategorySettingState, CategorySettingEvent, CategorySettingSideEffect>() {
    init {
        val category = savedStateHandle.toRoute<CategorySetting>(typeMap = mapOf(typeOf<CategoryIcon>() to CategoryIconNavType),)
        handleEvent(
            CategorySettingEvent.Init(
                categoryNo = category.categoryNo,
                categoryName = category.categoryName,
                categoryIcon = category.categoryIcon,
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
                        selectedCategoryIcon = event.categoryIcon,
                    )
                }
            }

            is CategorySettingEvent.ClickEdit -> {
                setEffect(CategorySettingSideEffect.ShowCategoryIconBottomSheet)
            }

            is CategorySettingEvent.ClickConfirm -> {
                if (state.value.isCreateMode()) {
                    viewModelScope.launch {
                        pomodoroSettingRepository.addPomodoroCategory(
                            event.categoryName,
                            state.value.selectedCategoryIcon.name
                        )
                    }
                } else {
                    // TODO 카테고리 수정 API
                }
                setEffect(CategorySettingSideEffect.GoToPomodoroSetting)
            }

            is CategorySettingEvent.SelectIcon -> {
                updateState { copy(selectedCategoryIcon = event.icon) }
                setEffect(CategorySettingSideEffect.DismissCategoryIconBottomSheet)
            }

            CategorySettingEvent.DismissBottomSheet -> {
                setEffect(CategorySettingSideEffect.DismissCategoryIconBottomSheet)
            }
        }
    }
}
