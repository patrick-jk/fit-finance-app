package com.fitfinance.app.data.repo

import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.domain.request.UserPutRequest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService) {
    suspend fun updateUser(userPutRequest: UserPutRequest, apiToken: String) = flow {
        try {
            val response = apiService.updateUser(userPutRequest, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun updatePassword(changePasswordRequest: ChangePasswordRequest, apiToken: String) = flow {
        try {
            val response = apiService.updatePassword(changePasswordRequest, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }
}