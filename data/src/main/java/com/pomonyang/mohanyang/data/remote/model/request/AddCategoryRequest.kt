package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AddCategoryRequest(
    val title: String,
    val iconType: String,
)
