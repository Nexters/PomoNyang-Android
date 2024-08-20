package com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid

interface DeviceIdLocalDataSource {
    suspend fun getDeviceId(): String
}
