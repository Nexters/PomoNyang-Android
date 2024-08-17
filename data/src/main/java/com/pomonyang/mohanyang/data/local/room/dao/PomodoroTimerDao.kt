package com.pomonyang.mohanyang.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity

@Dao
interface PomodoroTimerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoroSettingData(pomodoroTimerEntity: PomodoroTimerEntity)

    @Query("UPDATE pomodoro_timer SET focusedTime = :focusedTime WHERE focusTimeId = :focusTimeId")
    suspend fun updateFocusedTime(focusTimeId: String, focusedTime: String)

    @Query("UPDATE pomodoro_timer SET restedTime = :restedTime WHERE focusTimeId = :focusTimeId")
    suspend fun updateRestedTime(focusTimeId: String, restedTime: String)

    @Query("UPDATE pomodoro_timer SET doneAt = :doneAt WHERE focusTimeId = :focusTimeId")
    suspend fun updateDoneAt(focusTimeId: String, doneAt: String)
}
