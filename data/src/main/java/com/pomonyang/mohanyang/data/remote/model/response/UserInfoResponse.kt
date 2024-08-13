package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
class UserInfoResponse(
    val registeredDeviceNo: Int,
    val isPushEnabled: Boolean,
    val cat: CatTypeResponse
)
