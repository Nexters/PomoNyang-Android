package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val registeredDeviceNo: Int = -1,
    val isPushEnabled: Boolean = false,
    val cat: CatTypeResponse = CatTypeResponse()
)
