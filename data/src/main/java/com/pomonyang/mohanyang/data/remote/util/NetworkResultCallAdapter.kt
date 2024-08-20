package com.pomonyang.mohanyang.data.remote.util

import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter

internal class NetworkResultCallAdapter(
    private val responseType: Type
) : CallAdapter<Type, Call<Result<Type>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<Result<Type>> = NetworkResultCall(call)
}
