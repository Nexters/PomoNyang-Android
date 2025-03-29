package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSelectedPomodoroSettingUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
) {

    operator fun invoke(): Flow<PomodoroSettingEntity> {
        val settingListFlow: Flow<PomodoroSettingEntity> = pomodoroSettingRepository.getSelectedPomodoroSetting()
        return settingListFlow
    }
}
