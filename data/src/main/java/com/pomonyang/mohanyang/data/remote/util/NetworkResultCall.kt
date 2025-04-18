package com.pomonyang.mohanyang.data.remote.util

import com.mohanyang.data.BuildConfig
import com.pomonyang.mohanyang.data.remote.model.response.ErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class NetworkResultCall<T : Any>(
    private val proxy: Call<T>,
) : Call<Result<T>> {
    val json = Json {
        ignoreUnknownKeys = true // dto 정의되어 있지 않은 필드도 허락
        coerceInputValues = true // 해당 타입의 기본값으로 세팅되게
        isLenient = true // Json 규격에 안맞게 ex) "" 없이 들어온 경우에도 파싱 가능
        if (BuildConfig.DEBUG) prettyPrint = true
    }

    override fun enqueue(callback: Callback<Result<T>>) {
        proxy.enqueue(
            object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>,
                ) {
                    val networkResult = handleApi { response }
                    callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
                }

                override fun onFailure(
                    call: Call<T>,
                    t: Throwable,
                ) {
                    callback.onResponse(
                        this@NetworkResultCall,
                        Response.success(
                            Result.failure(t),
                        ),
                    )
                }
            },
        )
    }

    override fun execute(): Response<Result<T>> = throw NotImplementedError()

    override fun clone(): Call<Result<T>> = NetworkResultCall(proxy.clone())

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun cancel() {
        proxy.cancel()
    }

    private fun <T : Any> handleApi(execute: () -> Response<T>): Result<T> = try {
        val response = execute()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            Result.success(value = body)
        } else {
            val errorBodyMessage = response.errorBody()?.string()
            val errorResponse = json.decodeFromString<ErrorResponse>(errorBodyMessage.toString())
            Result.failure(
                when (response.code()) {
                    400 -> {
                        BadRequestException(errorResponse = errorResponse)
                    }

                    401 -> {
                        ForbiddenException(errorResponse = errorResponse)
                    }

                    500 -> {
                        InternalException(errorResponse = errorResponse)
                    }

                    else -> {
                        Exception(errorResponse.message)
                    }
                },

            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
