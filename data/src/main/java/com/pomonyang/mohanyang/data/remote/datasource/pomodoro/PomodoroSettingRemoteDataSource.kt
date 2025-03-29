package com.pomonyang.mohanyang.data.remote.datasource.pomodoro

import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse

interface PomodoroSettingRemoteDataSource {
    suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>>
    suspend fun modifyCategorySettingOption(
        categoryNo: Int,
        focusTime: String? = null,
        restTime: String? = null,
        iconType: String? = null,
        title: String? = null,
    ): Result<Unit>
    suspend fun addPomodoroCategory(
        title: String,
        iconType: String,
    ): Result<Unit>

    suspend fun deleteCategoryNo(categoryNumbers: List<Int>): Result<Unit>

    suspend fun updateSelectPomodoroCategory(categoryNo: Int): Result<Unit>
}
