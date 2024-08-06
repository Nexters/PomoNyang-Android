package com.pomonyang.mohanyang.data.repository

import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse

interface PomodoroSettingRepository {

    suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>>

    suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        focusTime: String,
        restTime: String
    ): Result<Unit>
}
