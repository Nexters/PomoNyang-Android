package com.pomonyang.mohanyang.data.local.room.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sample")
data class SampleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
