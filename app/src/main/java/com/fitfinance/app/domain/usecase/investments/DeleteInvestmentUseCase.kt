package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteInvestmentUseCase @Inject constructor(
    private val investmentRepository: InvestmentRepository
) : UseCase<Pair<Long, String>, Unit>() {
    override fun execute(param: Pair<Long, String>): Flow<Unit> {
        return investmentRepository.deleteInvestment(param.first, param.second)
    }
}