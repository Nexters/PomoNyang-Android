package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingRepository {

    fun getPomodoroSettingList(): Flow<List<PomodoroSettingEntity>>

    fun getSelectedPomodoroSetting(): Flow<PomodoroSettingEntity>

    suspend fun fetchPomodoroSettingList()

    suspend fun updatePomodoroCategorySetting(
        categoryNo: Int,
        title: String? = null,
        iconType: String? = null,
        focusTime: Int? = null,
        restTime: Int? = null,
    ): Result<Unit>

    suspend fun addPomodoroCategory(
        title: String,
        iconType: String,
    ): Result<Unit>

    suspend fun updateRecentUseCategoryNo(categoryNo: Int)

    suspend fun deleteCategoryNo(categoryNumbers: List<Int>)
}
