package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository

class DeleteFinanceUseCase(private val financeRepository: FinanceRepository) {
    suspend operator fun invoke(financeId: Long, apiToken: String) = financeRepository.deleteFinance(financeId, apiToken)
}