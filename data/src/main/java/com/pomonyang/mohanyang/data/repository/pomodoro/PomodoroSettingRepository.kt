package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingRepository {

    fun getRecentUseCategoryNo(): Flow<Int>

    suspend fun updateRecentUseCategoryNo(categoryNo: Int)

    fun getPomodoroSettingList(): Flow<List<PomodoroSettingEntity>>

    suspend fun fetchPomodoroSettingList()

    suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        titleName: String,
        focusTime: Int,
        restTime: Int
    ): Result<Unit>
}
