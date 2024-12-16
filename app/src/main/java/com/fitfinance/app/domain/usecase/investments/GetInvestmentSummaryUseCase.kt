package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.response.InvestmentSummaryResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvestmentSummaryUseCase @Inject constructor(
    private val investmentRepository: InvestmentRepository
) : UseCase<String, InvestmentSummaryResponse>() {
    override fun execute(param: String): Flow<InvestmentSummaryResponse> {
        return investmentRepository.getInvestmentSummary(param)
    }
}