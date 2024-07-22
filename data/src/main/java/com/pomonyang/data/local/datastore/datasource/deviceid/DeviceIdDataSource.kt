package com.pomonyang.data.local.datastore.datasource.deviceid

interface DeviceIdDataSource {
    suspend fun getDeviceId(): String
}
