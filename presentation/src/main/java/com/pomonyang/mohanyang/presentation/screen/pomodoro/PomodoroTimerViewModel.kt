package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.domain.usecase.InsertPomodoroInitialDataUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants
import com.pomonyang.mohanyang.presentation.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

data class PomodoroTimerState(
    val focusTime: Int = 0,
    val focusExceededTime: Int = 0,
    val restTime: Int = 0,
    val restExceededTime: Int = 0,
    val title: String = "",
    val categoryType: PomodoroCategoryType = PomodoroCategoryType.DEFAULT,
    val cat: CatType = CatType.CHEESE,
    val categoryNo: Int = -1
) : ViewState {
    fun displayFocusTime(): String = focusTime.formatTime()
    fun displayRestTime(): String = restTime.formatTime()
    fun displayFocusExceedTime(): String = focusExceededTime.formatTime()
    fun displayRestExceedTime(): String = restExceededTime.formatTime()
}

sealed interface PomodoroTimerEvent : ViewEvent {
    sealed interface PomodoroFocusEvent : PomodoroTimerEvent

    sealed interface PomodoroRestEvent : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    sealed interface PomodoroFocusEffect : PomodoroTimerEffect

    sealed interface PomodoroRestEffect : PomodoroTimerEffect
}

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val userRepository: UserRepository,
    private val pomodoroInitialDataUseCase: InsertPomodoroInitialDataUseCase
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {

    private var maxFocusTime: Int = 0
    private var maxRestTime: Int = 0
    private var currentFocusTimerId = UUID.randomUUID().toString()

    init {
        viewModelScope.launch {
            pomodoroInitialDataUseCase(currentFocusTimerId)
        }.invokeOnCompletion {
            loadPomodoroSettingData()
            collectTimer()
        }
    }

    private fun loadPomodoroSettingData() {
        viewModelScope.launch {
            val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
            val cat = userRepository.getMyInfo().cat.toModel()
            maxFocusTime = (selectedPomodoroSetting.focusTime.times(60))
            updateState {
                copy(
                    title = selectedPomodoroSetting.title,
                    categoryType = selectedPomodoroSetting.categoryType,
                    cat = cat.type
                )
            }
        }
    }

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
        // TODO [코니]
    }

    private fun collectTimer() {
        viewModelScope.launch {
            pomodoroTimerRepository.getPomodoroTimer(currentFocusTimerId).collect { timerData ->
                timerData ?: return@collect
                Timber.tag("koni").d("timerData $timerData")
                val currentFocusTime = timerData.focusedTime.coerceAtMost(maxFocusTime)
                val currentRestTime = timerData.restedTime.coerceAtMost(maxRestTime)
                val focusExceededTime = (timerData.focusedTime - maxFocusTime).coerceAtLeast(0)
                val restExceededTime = (timerData.focusedTime - maxRestTime).coerceAtLeast(0)
                if (timerData.focusedTime == maxFocusTime) {
                    Timber.tag("koni").d("timerData.focusedTime == maxFocusTime")
                    // TODO [코니] 포커스 알림 보내야 함
                }

                if (timerData.restedTime == maxRestTime) {
                    Timber.tag("koni").d("timerData.restedTime == maxRestTime")
                    // TODO [코니] 휴식 알림 보내야 함
                }
                updateState {
                    copy(
                        focusTime = currentFocusTime,
                        focusExceededTime = focusExceededTime,
                        restTime = currentRestTime,
                        restExceededTime = restExceededTime
                    )
                }

                if (focusExceededTime == PomodoroConstants.MAX_EXCEEDED_TIME) {
                    Timber.tag("koni").d("focusExceededTime == PomodoroConstants.MAX_EXCEEDED_TIME")
                    // TODO [코니] 여기 최대 초과시간 지났으면 홈으로 강제 이동
                }
            }
        }
    }
}
