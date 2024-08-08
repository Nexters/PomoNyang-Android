package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.domain.model.PomodoroCategoryModel
import com.pomonyang.mohanyang.domain.usecase.GetPomodoroSettingListUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

data class PomodoroState(
    val categoryList: List<PomodoroCategoryModel> = emptyList(),
    val selectedCategoryNo: Int = 0,
    val showCategoryBottomSheet: Boolean = false
) : ViewState

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

sealed interface PomodoroSideEffect : ViewSideEffect {
    data class ShowSnackBar(val message: String) : PomodoroSideEffect
}

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getPomodoroSettingListUseCase: GetPomodoroSettingListUseCase
) : BaseViewModel<PomodoroState, PomodoroEvent, PomodoroSideEffect>() {

    override fun setInitialState(): PomodoroState = PomodoroState()

    override fun handleEvent(event: PomodoroEvent) {
        Timber.tag("koni").d("handleEvent > $event")
        when (event) {
            PomodoroEvent.ClickCategory -> {
                updateState { copy(showCategoryBottomSheet = true) }
            }

            PomodoroEvent.ClickFocusTime -> TODO()
            PomodoroEvent.ClickMyInfo -> TODO()
            PomodoroEvent.ClickRestTime -> TODO()
            PomodoroEvent.ClickStartPomodoro -> TODO()
            PomodoroEvent.Init -> {
                collectCategoryData()
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
            setEffect(PomodoroSideEffect.ShowSnackBar("$title 카테고리로 변경했어요"))
        }
    }

    private fun collectCategoryData() {
        viewModelScope.launch {
            getPomodoroSettingListUseCase()
                .catch { setEffect(PomodoroSideEffect.ShowSnackBar("$it")) }
                .collect { result ->
                    val (data, recentCategoryNo) = result
                    updateState { copy(categoryList = data, selectedCategoryNo = recentCategoryNo) }
                }
        }
    }
}
