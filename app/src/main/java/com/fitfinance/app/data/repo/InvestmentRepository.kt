package com.fitfinance.app.data.repo

import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class InvestmentRepository(private val apiService: ApiService) {
    suspend fun getInvestmentsByUserId(apiToken: String) = flow {
        try {
            val response = apiService.getInvestmentsByUserId(apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun getInvestmentSummary(apiToken: String) = flow {
        try {
            val response = apiService.getInvestmentSummary(apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun createInvestment(investmentPostRequest: InvestmentPostRequest, apiToken: String) = flow {
        try {
            val response = apiService.createInvestment(investmentPostRequest, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun updateInvestment(investmentPutRequest: InvestmentPutRequest, apiToken: String) = flow {
        try {
            val response = apiService.updateInvestment(investmentPutRequest, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }

    suspend fun deleteInvestment(id: Long, apiToken: String) = flow {
        try {
            val response = apiService.deleteInvestment(id, apiToken)
            emit(response)
        } catch (e: HttpException) {
            emit(e)
        }
    }
}