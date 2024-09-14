package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.request.FinancePostRequest

class CreateFinanceUseCase(private val financeRepository: FinanceRepository) {
    suspend operator fun invoke(financePostRequest: FinancePostRequest, apiToken: String) = financeRepository.createFinance(financePostRequest, apiToken)
}