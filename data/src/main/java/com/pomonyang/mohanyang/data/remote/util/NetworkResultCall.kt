package com.pomonyang.mohanyang.data.remote.util

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class NetworkResultCall<T : Any>(
    private val proxy: Call<T>
) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        proxy.enqueue(
            object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>
                ) {
                    val networkResult = handleApi { response }
                    callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
                }

                override fun onFailure(
                    call: Call<T>,
                    t: Throwable
                ) {
                    callback.onResponse(
                        this@NetworkResultCall,
                        Response.success(
                            Result.failure(t)
                        )
                    )
                }
            }
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

    private fun <T : Any> handleApi(execute: () -> Response<T>): Result<T> =
        try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(value = body)
            } else {
                val errorBodyMessage = response.errorBody()?.string()
                Result.failure(
                    when (response.code()) {
                        400 -> {
                            BadRequestException(msg = "$errorBodyMessage / ${response.message()}")
                        }

                        500 -> {
                            InternalException(msg = "$errorBodyMessage / ${response.message()}")
                        }

                        401 -> {
                            ForbiddenException(msg = "$errorBodyMessage / ${response.message()}")
                        }

                        else -> {
                            Exception("${response.code()} / $errorBodyMessage / ${response.message()}")
                        }
                    }

                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
