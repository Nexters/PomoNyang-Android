package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetPomodoroSettingListUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) {

    operator fun invoke(): Flow<Pair<List<PomodoroSettingEntity>, Int>> {
        val settingListFlow = pomodoroSettingRepository.getPomodoroSettingList()
        val recentUseCategoryNoFlow = pomodoroSettingRepository.getRecentUseCategoryNo()

        return combine(settingListFlow, recentUseCategoryNoFlow) { settingList, recentUseCategoryNo ->
            val adjustedCategoryNo = if (recentUseCategoryNo == 0 && settingList.isNotEmpty()) {
                settingList.first().categoryNo
            } else {
                recentUseCategoryNo
            }
            settingList to adjustedCategoryNo
        }
    }
}
