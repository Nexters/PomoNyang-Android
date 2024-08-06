package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String = "",
    val accessTokenExpiredAt: String = "",
    val refreshToken: String = "",
    val refreshTokenExpiredAt: String = ""
)
