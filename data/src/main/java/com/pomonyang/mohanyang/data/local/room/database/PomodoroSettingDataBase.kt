package com.pomonyang.mohanyang.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity

@Database(entities = [PomodoroSettingEntity::class], version = 1, exportSchema = false)
internal abstract class PomodoroSettingDataBase : RoomDatabase() {
    abstract fun dao(): PomodoroSettingDao
}
