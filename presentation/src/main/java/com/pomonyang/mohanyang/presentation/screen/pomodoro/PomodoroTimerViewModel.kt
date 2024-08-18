package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
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
    data object Resume : PomodoroTimerEvent
    data object Pause : PomodoroTimerEvent
    data object Dispose : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    data class GoToPomodoroRest(
        val title: String,
        val focusTime: Int,
        val exceededTime: Int
    ) : PomodoroTimerEffect

    data object GoToPomodoroSetting : PomodoroTimerEffect
    data object StartFocusAlarm : PomodoroTimerEffect
    data object StopFocusAlarm : PomodoroTimerEffect
    data object SendEndFocusAlarm : PomodoroTimerEffect
}

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {

    private var timerJob: Job? = null

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
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
                pomodoroTimerRepository.insertPomodoroTimerInitData(selectedPomodoroSetting.categoryNo)
                startTimer()
            }

            PomodoroTimerEvent.ClickRest -> setEffect(
                PomodoroTimerEffect.GoToPomodoroRest(
                    title = state.value.title,
                    focusTime = state.value.currentFocusTime,
                    exceededTime = state.value.exceededTime
                )
            )

            PomodoroTimerEvent.ClickHome -> {
                viewModelScope.launch {
                    pomodoroTimerRepository.updatePomodoroDone()
                    pomodoroTimerRepository.savePomodoroCacheData()
                }
                setEffect(PomodoroTimerEffect.GoToPomodoroSetting)
            }
            PomodoroTimerEvent.Pause -> setEffect(PomodoroTimerEffect.StartFocusAlarm)
            PomodoroTimerEvent.Resume -> setEffect(PomodoroTimerEffect.StopFocusAlarm)
            PomodoroTimerEvent.Dispose -> setEffect(PomodoroTimerEffect.StopFocusAlarm)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(TIMER_DELAY)
                updateState {
                    if (currentFocusTime < maxFocusTime) {
                        val newFocusTime = currentFocusTime + ONE_SECOND
                        viewModelScope.launch {
                            pomodoroTimerRepository.updateFocusTime(newFocusTime)
                        }
                        copy(currentFocusTime = currentFocusTime + ONE_SECOND)
                    } else {
                        if (exceededTime == 0) {
                            setEffect(PomodoroTimerEffect.SendEndFocusAlarm)
                        }
                        val newExceedTime = exceededTime + ONE_SECOND
                        if (newExceedTime >= MAX_EXCEEDED_TIME) {
                            timerJob?.cancel()
                            setEffect(
                                PomodoroTimerEffect.GoToPomodoroRest(
                                    title = state.value.title,
                                    focusTime = state.value.currentFocusTime,
                                    exceededTime = newExceedTime
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
