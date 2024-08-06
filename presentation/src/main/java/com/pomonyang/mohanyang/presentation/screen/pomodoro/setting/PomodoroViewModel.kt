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

data class PomodoroState(
    val categoryList: List<PomodoroCategoryModel> = emptyList(),
    val selectedCategoryIndex: Int = 0
) : ViewState

sealed interface PomodoroEvent : ViewEvent {
    data object Init : PomodoroEvent
    data object ClickCategory : PomodoroEvent
    data object ClickRestTime : PomodoroEvent
    data object ClickFocusTime : PomodoroEvent
    data object ClickStartPomodoro : PomodoroEvent
    data object ClickMyInfo : PomodoroEvent
}

sealed interface PomodoroSideEffect : ViewSideEffect {
    data object GoToMyInfo : PomodoroSideEffect
    data class ShowSnackBar(val message: String) : PomodoroSideEffect
}

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val getPomodoroSettingListUseCase: GetPomodoroSettingListUseCase
) : BaseViewModel<PomodoroState, PomodoroEvent, PomodoroSideEffect>() {

    override fun setInitialState(): PomodoroState = PomodoroState()

    override suspend fun handleEvent(event: PomodoroEvent) {
        when (event) {
            PomodoroEvent.ClickCategory -> TODO()
            PomodoroEvent.ClickFocusTime -> TODO()
            PomodoroEvent.ClickMyInfo -> TODO()
            PomodoroEvent.ClickRestTime -> TODO()
            PomodoroEvent.ClickStartPomodoro -> TODO()
            PomodoroEvent.Init -> {
                updatePomodoroCategoryData()
            }
        }
    }

    private fun updatePomodoroCategoryData() {
        viewModelScope.launch {
            getPomodoroSettingListUseCase()
                .onSuccess { data ->
                    updateState { copy(categoryList = data) }
                }
                .onFailure {
                    setEffect(PomodoroSideEffect.ShowSnackBar("$it"))
                }
        }
    }
}
