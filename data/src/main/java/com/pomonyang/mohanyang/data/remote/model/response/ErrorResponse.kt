package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("type") val type: String,
    @SerialName("message") val message: String,
    @SerialName("errorTraceId") val errorTraceId: String,
)
