package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCategoryInfoRequest(
    val title: String? = null,
    val iconType: String? = null,
    val focusTime: String? = null,
    val restTime: String? = null,
)
