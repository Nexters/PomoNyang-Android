package com.pomonyang.mohanyang.data.local.datastore.datasource.token

interface TokenLocalDataSource {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun saveFcmToken(fcmToken: String)
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun getFcmToken(): String
    suspend fun clear()
}
