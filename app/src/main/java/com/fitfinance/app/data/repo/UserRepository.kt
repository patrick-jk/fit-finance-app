package com.fitfinance.app.data.repo

import com.fitfinance.app.data.local.dao.UserDao
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService, userDao: UserDao) {
    fun getUserInfo(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getUserInfo(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error getting user info")
        }
    }

    fun updateUser(userPutRequest: UserPutRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updateUser(userPutRequest, apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating user")
        }
    }

    fun updatePassword(changePasswordRequest: ChangePasswordRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updatePassword(changePasswordRequest, apiToken.toBearerToken()).enqueue(ApiCallback204Status(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating password")
        }
    }
}