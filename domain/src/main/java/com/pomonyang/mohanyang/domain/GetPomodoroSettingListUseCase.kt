package com.pomonyang.mohanyang.domain

import com.pomonyang.mohanyang.data.repository.PomodoroSettingRepository
import javax.inject.Inject

class GetPomodoroSettingListUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) {

    suspend operator fun invoke(): Result<Pair<List<PomodoroCategoryModel>, Int>> = pomodoroSettingRepository.getPomodoroSettingList()
        .mapCatching { pomodoroSettingList ->
            val categoryModels = pomodoroSettingList.map { it.toModel() }
            val recentUseCategoryNo = pomodoroSettingRepository.getRecentUseCategoryNo()

            val finalCategoryNo = if (recentUseCategoryNo == 0 && categoryModels.isNotEmpty()) {
                categoryModels.first().no
            } else {
                recentUseCategoryNo
            }

            categoryModels to finalCategoryNo
        }
}
