package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.domain.model.setting.PomodoroCategoryModel
import com.pomonyang.mohanyang.domain.model.setting.toModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

class GetSelectedPomodoroSettingUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) {

    operator fun invoke(): Flow<PomodoroCategoryModel> {
        val settingListFlow = pomodoroSettingRepository.getPomodoroSettingList()
            .map { list -> list.map { it.toModel() } }
        val recentUseCategoryNoFlow = pomodoroSettingRepository.getRecentUseCategoryNo()

        return settingListFlow.zip(recentUseCategoryNoFlow) { settingList, recentUseCategoryNo ->
            settingList.find { it.categoryNo == recentUseCategoryNo } ?: settingList.first()
        }
    }
}
