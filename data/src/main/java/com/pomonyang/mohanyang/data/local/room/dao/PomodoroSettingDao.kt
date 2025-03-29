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

    @Query("DELETE FROM pomodoro_setting")
    suspend fun deleteAllPomodoroSettings()

    @Query("SELECT * FROM pomodoro_setting")
    fun getPomodoroSetting(): Flow<List<PomodoroSettingEntity>>

    @Query("SELECT * FROM pomodoro_setting WHERE isSelected = 1")
    fun getSelectedPomodoroSetting(): Flow<PomodoroSettingEntity?>

    @Query("""
        UPDATE pomodoro_setting 
        SET isSelected = CASE 
            WHEN categoryNo = :selectedCategoryNo THEN 1 
            ELSE 0 
        END
    """)
    suspend fun updateSelectCategory(selectedCategoryNo: Int)

    @Query("SELECT * FROM pomodoro_setting LIMIT 1")
    suspend fun getFirstPomodoroSetting(): PomodoroSettingEntity?
}
