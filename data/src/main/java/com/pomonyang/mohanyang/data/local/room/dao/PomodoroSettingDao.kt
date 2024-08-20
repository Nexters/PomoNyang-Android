package com.pomonyang.mohanyang.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoroSettingData(pomodoroSettingEntity: List<PomodoroSettingEntity>)

    @Query("UPDATE pomodoro_setting SET focusTime = :focusTime, restTime = :restTime WHERE categoryNo = :categoryNo")
    suspend fun updateTimes(categoryNo: Int, focusTime: String, restTime: String)

    @Query("SELECT * FROM pomodoro_setting")
    fun getPomodoroSetting(): Flow<List<PomodoroSettingEntity>>
}
