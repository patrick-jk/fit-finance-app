package com.fitfinance.app.data.repo

import android.util.Log
import com.fitfinance.app.data.local.dao.FinanceDao
import com.fitfinance.app.data.local.dao.HomeSummaryDao
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class FinanceRepository @Inject constructor(
    private val apiService: ApiService,
    private val financeDao: FinanceDao,
    private val homeSummaryDao: HomeSummaryDao
) {
    private var ioDispatcher = Dispatchers.IO

    fun getFinancesByUserId(apiToken: String) = flow {
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

    fun getUserSummary(apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.getUserSummary(apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }

            val homeSummary = withContext(ioDispatcher) {
                homeSummaryDao.deleteHomeSummary()
                homeSummaryDao.insertHomeSummary(response.toHomeSummaryEntity())
                homeSummaryDao.getHomeSummary().toHomeSummaryResponse()
            }
            emit(homeSummary)
        } catch (e: Exception) {
            Log.i("FinanceRepository", "Error getting user summary from API, using local data")
            val localHomeSummary = homeSummaryDao.getHomeSummary().toHomeSummaryResponse()
            emit(localHomeSummary)
        }
    }

    fun createFinance(financePostRequest: FinancePostRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.createFinance(financePostRequest, apiToken.toBearerToken()).enqueue(ApiCallback(it))
            }
            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }

    fun updateFinance(financePutRequest: FinancePutRequest, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.updateFinance(financePutRequest, apiToken.toBearerToken()).enqueue(ApiCallbackNoContentOrOkStatus(it))
            }
            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }

    fun deleteFinance(financeId: Long, apiToken: String) = flow {
        try {
            val response = suspendCancellableCoroutine {
                apiService.deleteFinance(financeId, apiToken.toBearerToken()).enqueue(ApiCallbackNoContentOrOkStatus(it))
            }
            emit(response)
        } catch (e: HttpException) {
            throw e
        }
    }
}