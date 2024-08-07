package com.pomonyang.mohanyang.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity

@Dao
interface PomodoroSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoroSettingData(pomodoroSettingEntity: List<PomodoroSettingEntity>)

    @Update
    suspend fun updatePomodoroSettingData(pomodoroSettingEntity: PomodoroSettingEntity)

    @Query("SELECT * FROM pomodoro_setting")
    suspend fun getPomodoroSetting(): List<PomodoroSettingEntity>
}
