package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import kotlinx.coroutines.flow.Flow

interface PomodoroTimerRepository {
    suspend fun insertPomodoroTimerInitData(categoryNo: Int, pomodoroTimerId: String)
    suspend fun incrementFocusedTime(pomodoroTimerId: String)
    suspend fun incrementRestedTime(pomodoroTimerId: String)
    suspend fun updatePomodoroDone(pomodoroTimerId: String)
    suspend fun updateRecentPomodoroDone()
    suspend fun
        savePomodoroData(pomodoroTimerId: String)
    suspend fun savePomodoroCacheData()
    fun getPomodoroTimer(timerId: String): Flow<PomodoroTimerEntity?>
}
