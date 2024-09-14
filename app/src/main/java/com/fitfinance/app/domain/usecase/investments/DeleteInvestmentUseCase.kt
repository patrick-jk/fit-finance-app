package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository

class DeleteInvestmentUseCase(private val investmentRepository: InvestmentRepository) {
    suspend operator fun invoke(investmentId: Long, apiToken: String) = investmentRepository.deleteInvestment(investmentId, apiToken)
}