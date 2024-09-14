package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.request.InvestmentPostRequest

class CreateInvestmentUseCase(private val investmentRepository: InvestmentRepository) {
    suspend operator fun invoke(investmentPostRequest: InvestmentPostRequest, apiToken: String) = investmentRepository.createInvestment(investmentPostRequest, apiToken)
}