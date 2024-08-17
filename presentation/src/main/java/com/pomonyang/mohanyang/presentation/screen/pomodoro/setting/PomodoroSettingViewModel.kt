package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetPomodoroSettingListUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.model.user.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class PomodoroSettingState(
    val categoryList: List<PomodoroCategoryModel> = emptyList(),
    val selectedCategoryNo: Int = 0,
    val showCategoryBottomSheet: Boolean = false,
    val catName: String = "",
    val showOnboardingTooltip: Boolean = false
) : ViewState {
    fun getSelectedCategory() = categoryList.find { it.categoryNo == selectedCategoryNo }
}

sealed interface PomodoroSettingEvent : ViewEvent {
    data class Init(val isNewUser: Boolean) : PomodoroSettingEvent
    data object ClickCategory : PomodoroSettingEvent
    data object ClickRestTime : PomodoroSettingEvent
    data object ClickFocusTime : PomodoroSettingEvent
    data object ClickStartPomodoroSetting : PomodoroSettingEvent
    data object ClickMyInfo : PomodoroSettingEvent
    data object DismissCategoryDialog : PomodoroSettingEvent
    data object DismissOnBoardingTooltip : PomodoroSettingEvent
    data class ClickCategoryConfirmButton(val confirmCategoryNo: Int) : PomodoroSettingEvent
}

sealed interface PomodoroSettingSideEffect : ViewSideEffect {
    data object GoToPomodoro : PomodoroSettingSideEffect

    data class ShowSnackBar(
        val message: String,
        @DrawableRes val iconRes: Int
    ) : PomodoroSettingSideEffect

    data class GoTimeSetting(
        val isFocusTime: Boolean,
        val initialTime: Int,
        val category: String
    ) : PomodoroSettingSideEffect
}

@HiltViewModel
class PomodoroSettingViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getPomodoroSettingListUseCase: GetPomodoroSettingListUseCase,
    private val userRepository: UserRepository
) : BaseViewModel<PomodoroSettingState, PomodoroSettingEvent, PomodoroSettingSideEffect>() {

    override fun setInitialState(): PomodoroSettingState = PomodoroSettingState()

    override fun handleEvent(event: PomodoroSettingEvent) {
        when (event) {
            PomodoroSettingEvent.ClickCategory -> {
                updateState { copy(showCategoryBottomSheet = true) }
            }

            PomodoroSettingEvent.ClickFocusTime -> {
                handleTimeSetting(isFocusTime = true)
            }

            PomodoroSettingEvent.ClickRestTime -> {
                handleTimeSetting(isFocusTime = false)
            }

            PomodoroSettingEvent.ClickMyInfo -> TODO()

            PomodoroSettingEvent.ClickStartPomodoroSetting -> {
                state.value.getSelectedCategory()?.let {
                    setEffect(PomodoroSettingSideEffect.GoToPomodoro)
                }
            }

            is PomodoroSettingEvent.Init -> {
                collectCategoryData()
                getMyCatInfo()
                updateState { copy(showOnboardingTooltip = event.isNewUser) }
            }

            PomodoroSettingEvent.DismissCategoryDialog -> {
                updateState { copy(showCategoryBottomSheet = false) }
            }

            is PomodoroSettingEvent.ClickCategoryConfirmButton -> {
                handleCategoryConfirmButton(event)
                viewModelScope.launch {
                    pomodoroSettingRepository.updateRecentUseCategoryNo(event.confirmCategoryNo)
                }
                updateState { copy(showCategoryBottomSheet = false) }
            }

            PomodoroSettingEvent.DismissOnBoardingTooltip -> {
                updateState { copy(showOnboardingTooltip = false) }
            }
        }
    }

    private fun handleCategoryConfirmButton(event: PomodoroSettingEvent.ClickCategoryConfirmButton) {
        if (state.value.selectedCategoryNo != event.confirmCategoryNo) {
            val title = state.value.categoryList.find { it.categoryNo == event.confirmCategoryNo }?.title
            setEffect(PomodoroSettingSideEffect.ShowSnackBar("$title 카테고리로 변경했어요", R.drawable.ic_check))
        }
    }

    private fun handleTimeSetting(isFocusTime: Boolean) {
        state.value.getSelectedCategory()?.let {
            val initialTime = if (isFocusTime) it.focusTime else it.restTime
            setEffect(
                PomodoroSettingSideEffect.GoTimeSetting(isFocusTime = isFocusTime, initialTime = initialTime, category = it.title)
            )
        }
    }

    private fun collectCategoryData() {
        viewModelScope.launch {
            getPomodoroSettingListUseCase()
                .collect { result ->
                    val (data, recentCategoryNo) = result
                    updateState { copy(categoryList = data.map { it.toModel() }, selectedCategoryNo = recentCategoryNo) }
                }
        }
    }

    private fun getMyCatInfo() {
        viewModelScope.launch {
            userRepository.getMyInfo().let {
                updateState { copy(catName = it.toModel().cat.name) }
            }
        }
    }
}
