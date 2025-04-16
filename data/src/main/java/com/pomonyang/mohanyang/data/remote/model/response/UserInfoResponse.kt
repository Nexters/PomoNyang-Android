package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val registeredDeviceNo: Int = -1,
    val isPushEnabled: Boolean = false,
    val cat: CatTypeResponse = CatTypeResponse(),
) {
    fun isNewUser(): Boolean {
        /* 서버에서 fetch 한 기본 고양이 id가 -1 인 경우 신규 유저로 판단 */
        return this.cat.no == NEW_USER_VALIDATION_ID
    }

    companion object {
        const val NEW_USER_VALIDATION_ID = -1
    }
}
