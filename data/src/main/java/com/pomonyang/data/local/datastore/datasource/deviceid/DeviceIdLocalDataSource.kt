package com.pomonyang.data.local.datastore.datasource.deviceid

interface DeviceIdLocalDataSource {
    suspend fun getDeviceId(): String
}
