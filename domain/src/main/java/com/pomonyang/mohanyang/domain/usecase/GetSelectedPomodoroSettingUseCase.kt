package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class GetSelectedPomodoroSettingUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) {

    operator fun invoke(): Flow<PomodoroSettingEntity> {
        val settingListFlow = pomodoroSettingRepository.getPomodoroSettingList()
        val recentUseCategoryNoFlow = pomodoroSettingRepository.getRecentUseCategoryNo()

        return settingListFlow.zip(recentUseCategoryNoFlow) { settingList, recentUseCategoryNo ->
            settingList.find { it.categoryNo == recentUseCategoryNo } ?: settingList.first()
        }
    }
}
