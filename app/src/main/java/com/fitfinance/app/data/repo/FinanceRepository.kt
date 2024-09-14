package com.fitfinance.app.data.repo

import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FinanceRepository(private val apiService: ApiService) {
    suspend fun getFinancesByUserId(apiToken: String) = flow {
        try {
            val response = apiService.getFinancesByUserId(apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun getUserSummary(apiToken: String) = flow {
        try {
            val response = apiService.getUserSummary(apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun createFinance(financePostRequest: FinancePostRequest, apiToken: String) = flow {
        try {
            val response = apiService.createFinance(financePostRequest, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun updateFinance(financePutRequest: FinancePutRequest, apiToken: String) = flow {
        try {
            val response = apiService.updateFinance(financePutRequest, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun deleteFinance(financeId: Long, apiToken: String) = flow {
        try {
            val response = apiService.deleteFinance(financeId, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }
}