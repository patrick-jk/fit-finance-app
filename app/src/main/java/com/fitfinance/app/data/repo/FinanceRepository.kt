package com.fitfinance.app.data.repo

import android.util.Log
import com.fitfinance.app.data.local.dao.FinanceDao
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FinanceRepository(private val apiService: ApiService, private val financeDao: FinanceDao) {
    private var ioDispatcher = Dispatchers.IO

    suspend fun getFinancesByUserId(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getFinancesByUserId(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }

            val finances = withContext(ioDispatcher) {
                financeDao.deleteAllFinances()
                financeDao.insertAllFinances(response.map { it.toFinanceEntity() })
                Log.i("FinanceRepository", "Finances saved to database")
                financeDao.getFinances().map { it.toFinanceGetResponse() }
            }

            emit(finances)

        } catch (e: Exception) {
            Log.i("FinanceRepository", "Error getting finances from API, using local data")
            val localFinances = financeDao.getFinances().map { it.toFinanceGetResponse() }
            emit(localFinances)
        }
    }


    suspend fun getUserSummary(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getUserSummary(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error getting user summary")
        }
    }

    suspend fun createFinance(financePostRequest: FinancePostRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.createFinance(financePostRequest, apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error creating finance")
        }
    }

    suspend fun updateFinance(financePutRequest: FinancePutRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updateFinance(financePutRequest, apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating finance")
        }
    }

    suspend fun deleteFinance(financeId: Long, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.deleteFinance(financeId, apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error deleting finance")
        }
    }
}