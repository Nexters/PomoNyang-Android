package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPushTokenRequest(
    val deviceToken: String,
    val deviceType: String,
)
