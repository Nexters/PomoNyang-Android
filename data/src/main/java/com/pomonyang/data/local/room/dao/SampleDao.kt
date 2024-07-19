package com.pomonyang.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.pomonyang.data.local.room.enitity.SampleEntity

@Dao
interface SampleDao {
    @Insert
    suspend fun addSample(sample: SampleEntity)
}
