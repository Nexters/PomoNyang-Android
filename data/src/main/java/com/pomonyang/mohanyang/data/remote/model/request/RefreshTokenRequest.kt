package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: String)
