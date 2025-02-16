package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val deviceId: String,
)
