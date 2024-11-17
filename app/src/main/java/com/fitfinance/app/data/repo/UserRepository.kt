package com.fitfinance.app.data.repo

import android.util.Log
import com.fitfinance.app.data.local.dao.UserDao
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService, private val userDao: UserDao) {
    private var ioDispatcher = Dispatchers.IO

    fun getUserInfo(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getUserInfo(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }

            val user = withContext(ioDispatcher) {
                userDao.deleteUser()
                userDao.insertUser(response.toUserEntity())
                userDao.getUserInfo().toUserGetResponse()
            }
            emit(user)
        } catch (e: Exception) {
            Log.i("UserRepository", "Error getting user from API, using local data")
            val localUser = userDao.getUserInfo().toUserGetResponse()
            emit(localUser)
        }
    }

    fun updateUser(userPutRequest: UserPutRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updateUser(userPutRequest, apiToken.toBearerToken()).enqueue(ApiCallbackNoContentOrOkStatus(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating user")
        }
    }

    fun updatePassword(changePasswordRequest: ChangePasswordRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updatePassword(changePasswordRequest, apiToken.toBearerToken()).enqueue(ApiCallbackNoContentOrOkStatus(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating password")
        }
    }
}