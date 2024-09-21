package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class DeleteFinanceUseCase(private val financeRepository: FinanceRepository): UseCase<Pair<Long, String>, Call<Void>>() {
    override suspend fun execute(param: Pair<Long, String>): Flow<Call<Void>> {
        return financeRepository.deleteFinance(param.first, param.second)
    }
}