package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateFinanceUseCase @Inject constructor(
    private val financeRepository: FinanceRepository
) : UseCase<Pair<FinancePutRequest, String>, Unit>() {
    override fun execute(param: Pair<FinancePutRequest, String>): Flow<Unit> {
        return financeRepository.updateFinance(param.first, param.second)
    }
}