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

    @Query(
        """
        UPDATE pomodoro_setting 
        SET 
            focusTime = COALESCE(:focusTime, focusTime), 
            restTime = COALESCE(:restTime, restTime), 
            iconType = COALESCE(:iconType, iconType), 
            title = COALESCE(:title, title)
        WHERE categoryNo = :categoryNo
    """,
    )
    suspend fun updateSetting(
        categoryNo: Int,
        focusTime: String?,
        restTime: String?,
        iconType: String?,
        title: String?,
    )

    @Query("DELETE FROM pomodoro_setting")
    suspend fun deleteAllPomodoroSettings()

    @Query("SELECT * FROM pomodoro_setting")
    fun getPomodoroSetting(): Flow<List<PomodoroSettingEntity>>

    @Query("SELECT * FROM pomodoro_setting ORDER BY isSelected DESC, categoryNo ASC LIMIT 1")
    fun getSelectedPomodoroSetting(): Flow<PomodoroSettingEntity>

    @Query(
        """
        UPDATE pomodoro_setting 
        SET isSelected = CASE 
            WHEN categoryNo = :selectedCategoryNo THEN 1 
            ELSE 0 
        END
    """,
    )
    suspend fun updateSelectCategory(selectedCategoryNo: Int)

    @Query("SELECT * FROM pomodoro_setting LIMIT 1")
    suspend fun getFirstPomodoroSetting(): PomodoroSettingEntity?

    @Query("DELETE FROM pomodoro_setting WHERE categoryNo IN (:categoryNos)")
    suspend fun deletePomodoroSettingsByCategoryNos(categoryNos: List<Int>)
}
