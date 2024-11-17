package com.fitfinance.app.data.repo

import kotlinx.coroutines.CancellableContinuation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ApiCallbackNoContentOrOkStatus(private val continuation: CancellableContinuation<Unit>) : Callback<Unit> {
    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
        if (response.code() == 204 || response.code() == 200) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(HttpException(response))
        }
    }

    override fun onFailure(call: Call<Unit>, t: Throwable) {
        continuation.resumeWithException(t)
    }
}