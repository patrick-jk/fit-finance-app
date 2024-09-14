package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository

class GetInvestmentSummaryUseCase(private val investmentRepository: InvestmentRepository) {
    suspend operator fun invoke(apiToken: String) = investmentRepository.getInvestmentSummary(apiToken)
}