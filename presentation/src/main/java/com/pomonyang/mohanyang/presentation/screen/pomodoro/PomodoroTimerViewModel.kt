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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

data class PomodoroTimerState(
    val focusTime: Int = 0,
    val focusExceededTime: Int = 0,
    val maxFocusTime: Int = 0,
    val restTime: Int = 0,
    val restExceededTime: Int = 0,
    val maxRestTime: Int = 0,
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
    // 포커스, 휴식 이벤트 분리를 했는데 나중에 필요하면 상속받고 추가
    sealed interface PomodoroFocusEvent : PomodoroTimerEvent
    sealed interface PomodoroRestEvent : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    sealed interface PomodoroFocusEffect : PomodoroTimerEffect {
        data object ForceGoRest : PomodoroFocusEffect
    }

    sealed interface PomodoroRestEffect : PomodoroTimerEffect
}

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val userRepository: UserRepository,
    private val pomodoroInitialDataUseCase: InsertPomodoroInitialDataUseCase
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {

    private val currentFocusTimerId = MutableStateFlow("")

    private val combinedTimerData = currentFocusTimerId.flatMapLatest { timerId ->
        pomodoroTimerRepository.getPomodoroTimer(timerId)
    }

    init {
        loadPomodoroSettingData()
        initPomodoroData()
        loadTimerData()
    }

    private fun loadTimerData() {
        viewModelScope.launch {
            combinedTimerData.collect { timerData ->
                timerData?.let { updateTimerState(it) }
            }
        }
    }

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
    }

    private fun loadPomodoroSettingData() {
        viewModelScope.launch {
            getSelectedPomodoroSettingUseCase().collect { entity ->
                val selectedPomodoroSetting = entity.toModel()
                val cat = userRepository.getMyInfo().cat.toModel()
                updateState {
                    copy(
                        title = selectedPomodoroSetting.title,
                        categoryType = selectedPomodoroSetting.categoryType,
                        cat = cat.type,
                        maxFocusTime = (selectedPomodoroSetting.focusTime.times(60)),
                        maxRestTime = (selectedPomodoroSetting.restTime.times(60))
                    )
                }
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

    private fun updateTimerState(timerData: PomodoroTimerEntity) {
        val currentFocusTime = timerData.focusedTime.coerceAtMost(state.value.maxFocusTime)
        val currentRestTime = timerData.restedTime.coerceAtMost(state.value.maxRestTime)
        val focusExceededTime = (timerData.focusedTime - state.value.maxFocusTime).coerceAtLeast(0)
        val restExceededTime = (timerData.restedTime - state.value.maxRestTime).coerceAtLeast(0)

        updateState {
            copy(
                focusTime = currentFocusTime,
                focusExceededTime = focusExceededTime,
                restTime = currentRestTime,
                restExceededTime = restExceededTime
            )
        }

        if (focusExceededTime == PomodoroConstants.MAX_EXCEEDED_TIME) {
            savePomodoroData()
            setEffect(PomodoroTimerEffect.PomodoroFocusEffect.ForceGoRest)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun savePomodoroData() {
        GlobalScope.launch {
            pomodoroTimerRepository.updatePomodoroDone(currentFocusTimerId.value)
            pomodoroTimerRepository.savePomodoroData(currentFocusTimerId.value)
        }
    }

    override fun onCleared() {
        savePomodoroData()
        super.onCleared()
    }
}
