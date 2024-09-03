package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import kotlinx.coroutines.flow.Flow

interface PomodoroTimerRepository {
    suspend fun insertPomodoroTimerInitData(categoryNo: Int)
    suspend fun incrementFocusedTime()
    suspend fun incrementRestedTime()
    suspend fun updatePomodoroDone()
    suspend fun savePomodoroCacheData()
    fun getPomodoroTimer(): Flow<PomodoroTimerEntity?>
}
