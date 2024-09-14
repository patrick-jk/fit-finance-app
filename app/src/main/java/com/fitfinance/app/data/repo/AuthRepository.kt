package com.fitfinance.app.data.repo

import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.request.RegisterRequest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AuthRepository(private val apiService: ApiService) {
    suspend fun registerUser(registerRequest: RegisterRequest) = flow {
        try {
            val response = apiService.registerUser(registerRequest)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun authenticateUser(authenticationRequest: AuthenticationRequest) = flow {
        try {
            val response = apiService.authenticateUser(authenticationRequest)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }
}