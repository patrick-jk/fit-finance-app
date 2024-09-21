package com.fitfinance.app.data.repo

import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService) {
    suspend fun updateUser(userPutRequest: UserPutRequest, apiToken: String) = flow {
        try {
            val response = apiService.updateUser(userPutRequest, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating user")
        }
    }

    suspend fun updatePassword(changePasswordRequest: ChangePasswordRequest, apiToken: String) = flow {
        try {
            val response = apiService.updatePassword(changePasswordRequest, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating password")
        }
    }
}