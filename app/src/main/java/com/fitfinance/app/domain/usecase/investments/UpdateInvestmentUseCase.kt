package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.request.InvestmentPutRequest

class UpdateInvestmentUseCase(private val investmentRepository: InvestmentRepository) {
    suspend operator fun invoke(investmentPutRequest: InvestmentPutRequest, apiToken: String) = investmentRepository.updateInvestment(investmentPutRequest, apiToken)
}