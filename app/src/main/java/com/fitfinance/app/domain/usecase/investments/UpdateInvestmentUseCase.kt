package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class UpdateInvestmentUseCase(private val investmentRepository: InvestmentRepository): UseCase<Pair<InvestmentPutRequest, String>, Call<Void>>() {
    override suspend fun execute(param: Pair<InvestmentPutRequest, String>): Flow<Call<Void>> {
        return investmentRepository.updateInvestment(param.first, param.second)
    }
}