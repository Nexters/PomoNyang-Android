package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCategoryInfoRequest(
    val focusTime: String,
    val restTime: String,
)
