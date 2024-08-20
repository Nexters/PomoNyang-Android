package com.pomonyang.mohanyang.presentation.model.user

import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.toModel

data class UserInfoModel(
    val registeredDeviceNo: Int,
    val isPushEnabled: Boolean,
    val cat: CatInfoModel
)

fun UserInfoResponse.toModel() = UserInfoModel(
    registeredDeviceNo = this.registeredDeviceNo,
    isPushEnabled = this.isPushEnabled,
    cat = this.cat.toModel()
)
