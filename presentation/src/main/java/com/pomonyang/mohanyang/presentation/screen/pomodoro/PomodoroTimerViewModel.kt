package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

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
    data object Start : PomodoroTimerEvent

    // 포커스, 휴식 이벤트 분리를 했는데 나중에 필요하면 상속받고 추가
    sealed interface PomodoroFocusEvent : PomodoroTimerEvent
    sealed interface PomodoroRestEvent : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    sealed interface PomodoroFocusEffect : PomodoroTimerEffect {
        data object SendEndFocusAlarm : PomodoroFocusEffect
        data object ForceGoRest : PomodoroFocusEffect
    }

    sealed interface PomodoroRestEffect : PomodoroTimerEffect {
        data object SendEndRestAlarm : PomodoroRestEffect
    }
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
    private val currentFocusTimerId = MutableStateFlow("")

    private val combinedTimerData = currentFocusTimerId.flatMapLatest { timerId ->
        pomodoroTimerRepository.getPomodoroTimer(timerId)
    }

    init {
        viewModelScope.launch {
            combinedTimerData.collect { timerData ->
                timerData?.let { updateTimerState(it) }
            }
        }
    }

    private fun updateTimerState(timerData: PomodoroTimerEntity) {
        val currentFocusTime = timerData.focusedTime.coerceAtMost(maxFocusTime)
        val currentRestTime = timerData.restedTime.coerceAtMost(maxRestTime)
        val focusExceededTime = (timerData.focusedTime - maxFocusTime).coerceAtLeast(0)
        val restExceededTime = (timerData.restedTime - maxRestTime).coerceAtLeast(0)

        if (timerData.focusedTime == maxFocusTime) {
            setEffect(PomodoroTimerEffect.PomodoroFocusEffect.SendEndFocusAlarm)
        }

        if (timerData.restedTime == maxRestTime) {
            setEffect(PomodoroTimerEffect.PomodoroRestEffect.SendEndRestAlarm)
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
            setEffect(PomodoroTimerEffect.PomodoroFocusEffect.ForceGoRest)
        }
    }

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
        when (event) {
            PomodoroTimerEvent.Start -> {
                setInitialState()
                loadPomodoroSettingData()
                initPomodoroData()
            }
        }
    }

    private fun loadPomodoroSettingData() {
        viewModelScope.launch {
            val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
            val cat = userRepository.getMyInfo().cat.toModel()
            maxFocusTime = (selectedPomodoroSetting.focusTime.times(60))
            maxRestTime = (selectedPomodoroSetting.restTime.times(60))
            updateState {
                copy(
                    title = selectedPomodoroSetting.title,
                    categoryType = selectedPomodoroSetting.categoryType,
                    cat = cat.type
                )
            }
        }
    }

    private fun initPomodoroData() {
        viewModelScope.launch {
            val focusTimeKey = UUID.randomUUID().toString()
            currentFocusTimerId.value = focusTimeKey
            pomodoroInitialDataUseCase(focusTimeKey)
        }
    }
}
