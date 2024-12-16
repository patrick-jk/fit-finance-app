package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFinanceUseCase @Inject constructor(private val financeRepository: FinanceRepository) : UseCase<Pair<Long, String>, Unit>() {
    override fun execute(param: Pair<Long, String>): Flow<Unit> {
        return financeRepository.deleteFinance(param.first, param.second)
    }
}