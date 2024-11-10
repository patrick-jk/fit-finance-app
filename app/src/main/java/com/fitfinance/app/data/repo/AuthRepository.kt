package com.fitfinance.app.data.repo

import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException

class AuthRepository(private val apiService: ApiService) {
    suspend fun registerUser(registerRequest: RegisterRequest) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.registerUser(registerRequest).enqueue(ApiCallback(it))
            }

            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error registering user")
        }
    }

    suspend fun authenticateUser(authenticationRequest: AuthenticationRequest) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.authenticateUser(authenticationRequest).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error authenticating user")
        }
    }

    suspend fun refreshToken(token: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.refreshToken(token.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error refreshing token")
        }
    }
}