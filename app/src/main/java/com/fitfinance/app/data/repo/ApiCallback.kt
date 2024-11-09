package com.fitfinance.app.data.repo

import kotlinx.coroutines.CancellableContinuation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ApiCallback<T>(private val continuation: CancellableContinuation<T>) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            response.body()?.let { continuation.resume(it) } ?: continuation.resumeWithException(NullPointerException("Response body is null"))
        } else {
            continuation.resumeWithException(HttpException(response))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        continuation.resumeWithException(t)
    }
}