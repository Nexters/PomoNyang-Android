package com.pomonyang.mohanyang.data.local.datastore.datasource.user

import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse

internal interface UserLocalDataSource {
    suspend fun saveUserInfo(userInfo: UserInfoResponse)
    suspend fun getUserInfo(): UserInfoResponse
}
