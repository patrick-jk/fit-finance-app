package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteInvestmentUseCase(private val investmentRepository: InvestmentRepository) : UseCase<Pair<Long, String>, Unit>() {
    override suspend fun execute(param: Pair<Long, String>): Flow<Unit> {
        return investmentRepository.deleteInvestment(param.first, param.second)
    }
}