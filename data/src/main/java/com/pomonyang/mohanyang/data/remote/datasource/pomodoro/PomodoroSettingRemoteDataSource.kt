package com.pomonyang.mohanyang.data.remote.datasource.pomodoro

import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse

interface PomodoroSettingRemoteDataSource {
    suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>>
    suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        focusTime: String,
        restTime: String,
    ): Result<Unit>
    suspend fun addPomodoroCategory(
        title: String,
        iconType: String,
    ): Result<Unit>

    suspend fun deleteCategoryNo(categoryNumbers: List<Int>): Result<Unit>
}
