package com.pomonyang.mohanyang.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroTimerDao {

    @Query("SELECT * FROM pomodoro_timer")
    suspend fun getAllPomodoroTimers(): List<PomodoroTimerEntity>

    @Query("SELECT *FROM pomodoro_timer WHERE focusTimeId =:timerId")
    fun getTimer(timerId: String): Flow<PomodoroTimerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoroSettingData(pomodoroTimerEntity: PomodoroTimerEntity)

    @Query(
        """
            UPDATE pomodoro_timer 
            SET focusedTime = focusedTime + 1 
            WHERE focusTimeId = :pomodoroTimerId
        """
    )
    suspend fun incrementFocusedTime(pomodoroTimerId: String)

    @Query(
        """
            UPDATE pomodoro_timer 
            SET restedTime = restedTime + 1 
            WHERE focusTimeId = :pomodoroTimerId
        """
    )
    suspend fun incrementRestTime(pomodoroTimerId: String)

    @Query("UPDATE pomodoro_timer SET doneAt = :doneAt WHERE focusTimeId = :pomodoroTimerId")
    suspend fun updateDoneAt(doneAt: String, pomodoroTimerId: String)

    @Query("UPDATE pomodoro_timer SET doneAt = :doneAt WHERE focusTimeId = (SELECT focusTimeId FROM pomodoro_timer ORDER BY ROWID DESC LIMIT 1)")
    suspend fun updateDoneAtRecentPomodoro(doneAt: String)

    @Query("UPDATE pomodoro_timer SET doneAt = :doneAt WHERE focusTimeId = :pomodoroTimerId")
    suspend fun updateDoneAtPomodoro(doneAt: String, pomodoroTimerId: String)

    @Query("DELETE FROM pomodoro_timer")
    suspend fun deletePomodoroTimerAll()

    @Query("DELETE FROM pomodoro_timer WHERE focusTimeId = :focusTimeId")
    suspend fun deletePomodoroTimer(focusTimeId: String)
}
