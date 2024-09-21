package com.fitfinance.app.data.repo

import android.util.Log
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AuthRepository(private val apiService: ApiService) {
    suspend fun registerUser(registerRequest: RegisterRequest) = flow {
        try {
            val response = apiService.registerUser(registerRequest)
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error registering user")
        }
    }

    suspend fun authenticateUser(authenticationRequest: AuthenticationRequest) = flow {
        Log.i("AuthRepository", "authenticateUser: $authenticationRequest")
        try {
            val response = apiService.authenticateUser(authenticationRequest)
            Log.i("AuthRepository", "inside try authenticateUser: $response")
            emit(response)
        } catch (e: HttpException) {
            Log.e("AuthRepository", "authenticateUser: ${e.message()}")
            e.throwRemoteException("Error authenticating user")
        }
    }

    suspend fun refreshToken(token: String) = flow {
        try {
            Log.i("AuthRepository", "refreshToken: $token")
            val response = apiService.refreshToken(token.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error refreshing token")
        }
    }
}