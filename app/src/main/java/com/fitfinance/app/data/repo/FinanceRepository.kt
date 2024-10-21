package com.fitfinance.app.data.repo

import android.util.Log
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FinanceRepository(private val apiService: ApiService) {
    suspend fun getFinancesByUserId(apiToken: String) = flow {
        try {
            val response = apiService.getFinancesByUserId(apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error getting finances")
        }
    }

    suspend fun getUserSummary(apiToken: String) = flow {
        try {
            val response = apiService.getUserSummary(apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error getting user summary")
        }
    }

    suspend fun createFinance(financePostRequest: FinancePostRequest, apiToken: String) = flow {
        try {
            val response = apiService.createFinance(financePostRequest, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error creating finance")
        }
    }

    suspend fun updateFinance(financePutRequest: FinancePutRequest, apiToken: String) = flow {
        try {
            val response = apiService.updateFinance(financePutRequest, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error updating finance")
        }
    }

    suspend fun deleteFinance(financeId: Long, apiToken: String) = flow {
        try {
            val response = apiService.deleteFinance(financeId, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Error deleting finance")
        }
    }
}