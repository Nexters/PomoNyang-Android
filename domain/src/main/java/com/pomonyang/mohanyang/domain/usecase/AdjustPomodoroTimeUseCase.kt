package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import java.time.Duration
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import timber.log.Timber

class AdjustPomodoroTimeUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) {

    suspend operator fun invoke(isFocusTime: Boolean, isIncrease: Boolean) {
        val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first()
        val adjustment = if (isIncrease) ADJUST_TIME_UNIT else -ADJUST_TIME_UNIT
        val focusTime = Duration.parse(selectedPomodoroSetting.focusTime).toMinutes()
        val restTime = Duration.parse(selectedPomodoroSetting.restTime).toMinutes()

        val updatedFocusTime = if (isFocusTime) focusTime + adjustment else focusTime
        val updatedRestTime = if (!isFocusTime) restTime + adjustment else restTime

        Timber.tag("koni").d("AdjustPomodoroTimeUseCase > $updatedFocusTime // $updatedRestTime")

        pomodoroSettingRepository.updatePomodoroCategoryTimes(
            categoryNo = selectedPomodoroSetting.categoryNo,
            focusTime = updatedFocusTime.toInt(),
            restTime = updatedRestTime.toInt()
        )
    }

    companion object {
        private const val ADJUST_TIME_UNIT = 5
    }
}
