package com.pomonyang.mohanyang.data.remote.model.response

import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import kotlinx.serialization.Serializable

@Serializable
data class PomodoroSettingResponse(
    val no: Int = -1,
    val title: String = "",
    val focusTime: String = "",
    val restTime: String = "",
)

internal fun PomodoroSettingResponse.toEntity() = PomodoroSettingEntity(
    categoryNo = no,
    title = title,
    focusTime = focusTime,
    restTime = restTime,
)
