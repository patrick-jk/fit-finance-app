package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.response.InvestmentSummaryResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class GetInvestmentSummaryUseCase(private val investmentRepository: InvestmentRepository) : UseCase<String, Call<InvestmentSummaryResponse>>() {
    override suspend fun execute(param: String): Flow<Call<InvestmentSummaryResponse>> {
        return investmentRepository.getInvestmentSummary(param)
    }
}