package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository

class GetUserSummaryUseCase(private val financeRepository: FinanceRepository) {
    suspend operator fun invoke(apiToken: String) = financeRepository.getUserSummary(apiToken)
}