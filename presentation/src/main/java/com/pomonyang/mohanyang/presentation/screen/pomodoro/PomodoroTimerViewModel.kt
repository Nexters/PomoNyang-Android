package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

data class PomodoroTimerState(
    val maxFocusTime: Int = 0,
    val currentFocusTime: Int = 0,
    val exceededTime: Int = 0,
    val title: String = "",
    val type: PomodoroCategoryType = PomodoroCategoryType.DEFAULT,
    val categoryNo: Int = -1
) : ViewState {
    fun displayFocusTime(): String = currentFocusTime.formatTime()
    fun displayExceedTime(): String = exceededTime.formatTime()
}

sealed interface PomodoroTimerEvent : ViewEvent {
    data object Init : PomodoroTimerEvent
    data object ClickRest : PomodoroTimerEvent
    data object ClickHome : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    data class GoToPomodoroRest(
        val type: String,
        val focusTime: Int,
        val exceededTime: Int
    ) : PomodoroTimerEffect

    data object GoToPomodoroSetting : PomodoroTimerEffect
}

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {

    private var timerJob: Job? = null

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
        Timber.tag("koni").d("handleEvent > $event")
        when (event) {
            PomodoroTimerEvent.Init -> viewModelScope.launch {
                val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
                updateState {
                    copy(
                        title = selectedPomodoroSetting.title,
                        type = selectedPomodoroSetting.categoryType,
                        maxFocusTime = (selectedPomodoroSetting.focusTime.times(60)),
                        categoryNo = selectedPomodoroSetting.categoryNo
                    )
                }
                startTimer()
            }

            PomodoroTimerEvent.ClickRest -> setEffect(
                PomodoroTimerEffect.GoToPomodoroRest(
                    type = state.value.type,
                    focusTime = state.value.currentFocusTime,
                    exceededTime = state.value.exceededTime
                )
            )

            PomodoroTimerEvent.ClickHome -> setEffect(PomodoroTimerEffect.GoToPomodoroSetting)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(TIMER_DELAY)
                updateState {
                    if (currentFocusTime < maxFocusTime) {
                        copy(currentFocusTime = currentFocusTime + ONE_SECOND)
                    } else {
                        val newExceedTime = exceededTime + ONE_SECOND
                        if (newExceedTime >= MAX_EXCEEDED_TIME) {
                            timerJob?.cancel()
                            setEffect(
                                PomodoroTimerEffect.GoToPomodoroRest(
                                    type = state.value.type,
                                    focusTime = state.value.currentFocusTime,
                                    exceededTime = state.value.exceededTime
                                )
                            )
                        }
                        copy(exceededTime = newExceedTime)
                    }
                }
            }
        }
    }
}
