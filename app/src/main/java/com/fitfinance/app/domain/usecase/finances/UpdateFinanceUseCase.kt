package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class UpdateFinanceUseCase(private val financeRepository: FinanceRepository): UseCase<Pair<FinancePutRequest, String>, Call<Void>>() {
    override suspend fun execute(param: Pair<FinancePutRequest, String>): Flow<Call<Void>> {
        return financeRepository.updateFinance(param.first, param.second)
    }
}