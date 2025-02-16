package com.pomonyang.mohanyang.presentation.screen.home.setting

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
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class PomodoroSettingState(
    val categoryList: List<PomodoroCategoryModel> = emptyList(),
    val selectedCategoryNo: Int = 0,
    val showCategoryBottomSheet: Boolean = false,
    val cat: CatInfoModel = CatInfoModel(
        no = -1,
        name = "",
        type = CatType.CHEESE,
    ),
    val isEndOnBoardingTooltip: Boolean = false,
) : ViewState {
    fun getSelectedCategory() = categoryList.find { it.categoryNo == selectedCategoryNo }
}

sealed interface PomodoroSettingEvent : ViewEvent {
    data object Init : PomodoroSettingEvent
    data object ClickCategory : PomodoroSettingEvent
    data object ClickRestTime : PomodoroSettingEvent
    data object ClickFocusTime : PomodoroSettingEvent
    data object ClickStartPomodoroSetting : PomodoroSettingEvent
    data object ClickMyInfo : PomodoroSettingEvent
    data object DismissCategoryDialog : PomodoroSettingEvent
    data object DismissOnBoardingTooltip : PomodoroSettingEvent
    data class ClickCategoryConfirmButton(val confirmCategoryNo: Int) : PomodoroSettingEvent
    data object ClickMenu : PomodoroSettingEvent
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
}

@HiltViewModel
class PomodoroSettingViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getPomodoroSettingListUseCase: GetPomodoroSettingListUseCase,
    private val userRepository: UserRepository,
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
                updateState { copy(isEndOnBoardingTooltip = true) }
            }

            PomodoroSettingEvent.ClickMenu -> {
                setEffect(PomodoroSettingSideEffect.GoToMyPage)
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
                PomodoroSettingSideEffect.GoTimeSetting(isFocusTime = isFocusTime, initialTime = initialTime, category = it.title),
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
            val cat = userRepository.getMyInfo().cat.toModel()
            updateState { copy(cat = cat) }
        }
    }
}
