package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.domain.GetPomodoroSettingListUseCase
import com.pomonyang.mohanyang.domain.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
                updatePomodoroCategoryData()
            }

            PomodoroEvent.DismissCategoryDialog -> {
                updateState { copy(showCategoryBottomSheet = false) }
            }

            is PomodoroEvent.ClickCategoryConfirmButton -> {
                if (state.value.selectedCategoryNo != event.confirmCategoryNo) {
                    // TODO 지훈 여기 나중에 리소스로 변경
                    setEffect(PomodoroSideEffect.ShowSnackBar("카테고리를 변경했어요"))
                }
                updateState { copy(showCategoryBottomSheet = false, selectedCategoryNo = event.confirmCategoryNo) }
            }
        }
    }

    private fun updatePomodoroCategoryData() {
        viewModelScope.launch {
            getPomodoroSettingListUseCase()
                .onSuccess { data ->
                    // 로컬 저장 로직이 우선 없어서 첫번째 아이템으로 설정
                    updateState { copy(categoryList = data, selectedCategoryNo = data.first().no) }
                }
                .onFailure {
                    setEffect(PomodoroSideEffect.ShowSnackBar("$it"))
                }
        }
    }
}
