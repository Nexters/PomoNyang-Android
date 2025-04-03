package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteCategoryRequest(
    val no: List<Int>,
)
