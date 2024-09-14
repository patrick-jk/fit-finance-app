package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository

class GetFinancesByUserIdUseCase(private val financeRepository: FinanceRepository) {
    suspend operator fun invoke(apiToken: String) = financeRepository.getFinancesByUserId(apiToken)
}