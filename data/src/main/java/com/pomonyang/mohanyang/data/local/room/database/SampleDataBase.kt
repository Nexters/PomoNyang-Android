package com.pomonyang.mohanyang.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pomonyang.mohanyang.data.local.room.dao.SampleDao
import com.pomonyang.mohanyang.data.local.room.enitity.SampleEntity

@Database(entities = [SampleEntity::class], version = 1, exportSchema = false)
internal abstract class SampleDataBase : RoomDatabase() {
    abstract fun dao(): SampleDao
}
