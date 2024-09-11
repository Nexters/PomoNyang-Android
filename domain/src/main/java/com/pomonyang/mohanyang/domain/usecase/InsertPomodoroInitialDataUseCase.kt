package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class InsertPomodoroInitialDataUseCase @Inject constructor(
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) {

    suspend operator fun invoke(focusTimeId: String) {
        val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().categoryNo
        pomodoroTimerRepository.insertPomodoroTimerInitData(selectedPomodoroSetting, focusTimeId)
    }
}
