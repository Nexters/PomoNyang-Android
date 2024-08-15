package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class AdjustPomodoroTimeUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) {

    suspend operator fun invoke(isFocusTime: Boolean, isIncrease: Boolean) {
        val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first()

        val adjustment = if (isIncrease) ADJUST_TIME_UNIT else -ADJUST_TIME_UNIT

        val updatedFocusTime = if (isFocusTime) selectedPomodoroSetting.focusTime + adjustment else selectedPomodoroSetting.focusTime
        val updatedRestTime = if (!isFocusTime) selectedPomodoroSetting.restTime + adjustment else selectedPomodoroSetting.restTime

        pomodoroSettingRepository.updatePomodoroCategoryTimes(
            categoryNo = selectedPomodoroSetting.categoryNo,
            focusTime = updatedFocusTime,
            restTime = updatedRestTime
        )
    }

    companion object {
        private const val ADJUST_TIME_UNIT = 5
    }
}
