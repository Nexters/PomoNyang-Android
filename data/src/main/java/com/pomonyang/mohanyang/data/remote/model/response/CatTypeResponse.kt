package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CatTypeResponse(
    val no: Int = -1,
    val name: String = "",
    val type: String = "",
)
