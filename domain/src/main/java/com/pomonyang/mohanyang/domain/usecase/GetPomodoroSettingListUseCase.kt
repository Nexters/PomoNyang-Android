package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.domain.model.PomodoroCategoryModel
import com.pomonyang.mohanyang.domain.model.toModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetPomodoroSettingListUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) {

    operator fun invoke(): Flow<Pair<List<PomodoroCategoryModel>, Int>> {
        val settingListFlow = pomodoroSettingRepository.getPomodoroSettingList()
            .map { list -> list.map { it.toModel() } }
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
