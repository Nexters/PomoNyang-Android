package com.pomonyang.mohanyang.domain.model

import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse

data class UserInfoModel(
    val registeredDeviceNo: Int,
    val isPushEnabled: Boolean,
    val cat: CatInfoModel
)

fun UserInfoResponse.toModel() = UserInfoModel(
    registeredDeviceNo = registeredDeviceNo,
    isPushEnabled = isPushEnabled,
    cat = cat.toModel()
)
