package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.model.setting.PomodoroCategoryModel
import com.pomonyang.mohanyang.domain.model.user.toModel
import com.pomonyang.mohanyang.domain.usecase.GetPomodoroSettingListUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class PomodoroState(
    val categoryList: List<PomodoroCategoryModel> = emptyList(),
    val selectedCategoryNo: Int = 0,
    val showCategoryBottomSheet: Boolean = false,
    val catName: String = ""
) : ViewState {
    fun getSelectedCategory() = categoryList.find { it.categoryNo == selectedCategoryNo }
}

sealed interface PomodoroEvent : ViewEvent {
    data object Init : PomodoroEvent
    data object ClickCategory : PomodoroEvent
    data object ClickRestTime : PomodoroEvent
    data object ClickFocusTime : PomodoroEvent
    data object ClickStartPomodoro : PomodoroEvent
    data object ClickMyInfo : PomodoroEvent
    data object DismissCategoryDialog : PomodoroEvent
    data class ClickCategoryConfirmButton(val confirmCategoryNo: Int) : PomodoroEvent
}

sealed interface PomodoroSettingSideEffect : ViewSideEffect {
    data class ShowSnackBar(val message: String) : PomodoroSettingSideEffect
    data class GoTimeSetting(
        val isFocusTime: Boolean,
        val type: String,
        val focusMinute: Long,
        val restMinute: Long,
        val categoryNo: Int
    ) : PomodoroSettingSideEffect
}

@HiltViewModel
class PomodoroSettingViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getPomodoroSettingListUseCase: GetPomodoroSettingListUseCase,
    private val userRepository: UserRepository
) : BaseViewModel<PomodoroState, PomodoroEvent, PomodoroSettingSideEffect>() {

    override fun setInitialState(): PomodoroState = PomodoroState()

    override fun handleEvent(event: PomodoroEvent) {
        when (event) {
            PomodoroEvent.ClickCategory -> {
                updateState { copy(showCategoryBottomSheet = true) }
            }

            PomodoroEvent.ClickFocusTime -> {
                handleTimeSetting(isFocusTime = true)
            }

            PomodoroEvent.ClickRestTime -> {
                handleTimeSetting(isFocusTime = false)
            }

            PomodoroEvent.ClickMyInfo -> TODO()

            PomodoroEvent.ClickStartPomodoro -> TODO()

            PomodoroEvent.Init -> {
                collectCategoryData()
                getMyCatInfo()
            }

            PomodoroEvent.DismissCategoryDialog -> {
                updateState { copy(showCategoryBottomSheet = false) }
            }

            is PomodoroEvent.ClickCategoryConfirmButton -> {
                handleCategoryConfirmButton(event)
                viewModelScope.launch {
                    pomodoroSettingRepository.updateRecentUseCategoryNo(event.confirmCategoryNo)
                }
                updateState { copy(showCategoryBottomSheet = false) }
            }
        }
    }

    private fun handleCategoryConfirmButton(event: PomodoroEvent.ClickCategoryConfirmButton) {
        if (state.value.selectedCategoryNo != event.confirmCategoryNo) {
            val title = state.value.categoryList.find { it.categoryNo == event.confirmCategoryNo }?.title
            // TODO 지훈 여기 나중에 리소스로 변경
            setEffect(PomodoroSettingSideEffect.ShowSnackBar("$title 카테고리로 변경했어요"))
        }
    }

    private fun handleTimeSetting(isFocusTime: Boolean) {
        state.value.getSelectedCategory()?.let {
            setEffect(
                PomodoroSettingSideEffect.GoTimeSetting(
                    isFocusTime = isFocusTime,
                    type = it.title,
                    focusMinute = it.focusTime,
                    restMinute = it.restTime,
                    categoryNo = it.categoryNo
                )
            )
        } ?: setEffect(PomodoroSettingSideEffect.ShowSnackBar("카테고리를 설정해주세요"))
    }

    private fun collectCategoryData() {
        viewModelScope.launch {
            getPomodoroSettingListUseCase()
                .catch { setEffect(PomodoroSettingSideEffect.ShowSnackBar("$it")) }
                .collect { result ->
                    val (data, recentCategoryNo) = result
                    updateState { copy(categoryList = data, selectedCategoryNo = recentCategoryNo) }
                }
        }
    }

    private fun getMyCatInfo() {
        viewModelScope.launch {
            userRepository.getMyInfo()
                .onSuccess {
                    updateState { copy(catName = it.toModel().cat.name) }
                }
                .onFailure {
                    setEffect(PomodoroSideEffect.ShowSnackBar("$it"))
                }
        }
    }
}
