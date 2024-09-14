package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.request.FinancePutRequest

class UpdateFinanceUseCase(private val financeRepository: FinanceRepository) {
    suspend operator fun invoke(financePutRequest: FinancePutRequest, apiToken: String) = financeRepository.updateFinance(financePutRequest, apiToken)
}