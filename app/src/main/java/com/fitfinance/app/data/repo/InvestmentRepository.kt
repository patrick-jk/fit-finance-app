package com.fitfinance.app.data.repo

import com.fitfinance.app.data.local.dao.InvestmentDao
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.util.throwRemoteException
import com.fitfinance.app.util.toBearerToken
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.HttpException

class InvestmentRepository(private val apiService: ApiService, private val investmentDao: InvestmentDao) {
    suspend fun getInvestmentsByUserId(apiToken: String) = flow {
        try {
            val response = apiService.getInvestmentsByUserId(apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Failed to get investments")
        }
    }

    suspend fun getInvestmentSummary(apiToken: String) = flow {
        try {
            val response = apiService.getInvestmentSummary(apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Failed to get investment summary")
        }
    }

    suspend fun createInvestment(investmentPostRequest: InvestmentPostRequest, apiToken: String) = flow {
        try {
            val response = apiService.createInvestment(investmentPostRequest, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Failed to create investment")
        }
    }

    fun updateInvestment(investmentPutRequest: InvestmentPutRequest, apiToken: String) = flow {
        try {
            val response = apiService.updateInvestment(investmentPutRequest, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Failed to update investment")
        }
    }

    fun deleteInvestment(id: Long, apiToken: String) = flow<Call<Unit>> {
        try {
            val response = apiService.deleteInvestment(id, apiToken.toBearerToken())
            emit(response)
        } catch (e: HttpException) {
            e.throwRemoteException("Failed to delete investment")
        }
    }
}