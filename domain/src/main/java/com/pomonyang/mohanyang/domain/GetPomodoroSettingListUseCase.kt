package com.pomonyang.mohanyang.domain

import com.pomonyang.mohanyang.data.repository.PomodoroSettingRepository
import javax.inject.Inject

class GetPomodoroSettingListUseCase @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) {

    suspend operator fun invoke(): Result<List<PomodoroCategoryModel>> = pomodoroSettingRepository.getPomodoroSettingList()
        .mapCatching { pomodoroSettingList ->
            pomodoroSettingList.map {
                it.toModel()
            }
        }
}
