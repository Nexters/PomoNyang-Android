package com.pomonyang.mohanyang.data.repository

import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse

interface PomodoroSettingRepository {

    suspend fun getRecentUseCategoryNo(): Int

    suspend fun updateRecentUseCategoryNo(categoryNo: Int)

    suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>>

    suspend fun fetchPomodoroSettingList(): Result<List<PomodoroSettingResponse>>

    suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        titleName: String,
        focusTime: String,
        restTime: String
    ): Result<Unit>
}
