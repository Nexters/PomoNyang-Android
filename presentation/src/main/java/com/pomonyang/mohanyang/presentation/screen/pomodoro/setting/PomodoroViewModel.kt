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
    data object ClickCategoryConfirmButton : PomodoroEvent
    data class ClickNewCategory(val categoryNo: Int) : PomodoroEvent
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

            is PomodoroEvent.ClickNewCategory -> {
                updateState { copy(selectedCategoryNo = event.categoryNo) }
            }

            PomodoroEvent.ClickCategoryConfirmButton -> {
                updateState { copy(showCategoryBottomSheet = false) }
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
