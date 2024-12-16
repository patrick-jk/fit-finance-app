package com.fitfinance.app.data.repo

import android.util.Log
import com.fitfinance.app.data.local.dao.InvestmentDao
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class InvestmentRepository @Inject constructor(
    private val apiService: ApiService, private val investmentDao: InvestmentDao
) {
    private var ioDispatcher = Dispatchers.IO

    fun getInvestmentsByUserId(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getInvestmentsByUserId(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }

            val investments = withContext(ioDispatcher) {
                investmentDao.deleteAllInvestments()
                investmentDao.insertAllInvestments(response.map { it.toInvestmentEntity() })
                Log.i("InvestmentRepository", "Investments saved to database")
                investmentDao.getInvestments().map { it.toInvestmentGetResponse() }
            }
            emit(investments)

        } catch (e: Exception) {
            Log.i("InvestmentRepository", "Error getting investments from API, using local data")
            val localInvestments = investmentDao.getInvestments().map { it.toInvestmentGetResponse() }
            emit(localInvestments)
        }
    }

    fun getInvestmentSummary(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getInvestmentSummary(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }

            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }

    fun createInvestment(investmentPostRequest: InvestmentPostRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.createInvestment(investmentPostRequest, apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }

            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }

    fun updateInvestment(investmentPutRequest: InvestmentPutRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updateInvestment(investmentPutRequest, apiToken.toBearerToken()).enqueue(ApiCallbackNoContentOrOkStatus(it))
            }
            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }

    fun deleteInvestment(id: Long, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.deleteInvestment(id, apiToken.toBearerToken()).enqueue(ApiCallbackNoContentOrOkStatus(it))
            }

            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }
}