package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
class CatInfoResponse(
    val no: Int,
    val catType: CatTypeResponse,
    val name: String
)

@Serializable
enum class CatTypeResponse {
    CHEESE,
    BLACK,
    THREE_COLOR
}
