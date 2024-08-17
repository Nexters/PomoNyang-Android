package com.pomonyang.mohanyang.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pomonyang.mohanyang.data.local.room.dao.PomodoroTimerDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity

@Database(entities = [PomodoroTimerEntity::class], version = 1, exportSchema = false)
internal abstract class PomodoroTimerDataBase : RoomDatabase() {
    abstract fun dao(): PomodoroTimerDao
}
