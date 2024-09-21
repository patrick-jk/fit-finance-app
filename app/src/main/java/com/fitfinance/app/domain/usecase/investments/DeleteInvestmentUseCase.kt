package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class DeleteInvestmentUseCase(private val investmentRepository: InvestmentRepository): UseCase<Pair<Long, String>, Call<Void>>() {
    override suspend fun execute(param: Pair<Long, String>): Flow<Call<Void>> {
        return investmentRepository.deleteInvestment(param.first, param.second)
    }
}