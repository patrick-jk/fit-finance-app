package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class GetInvestmentsByUserIdUseCase(private val investmentRepository: InvestmentRepository): UseCase<String, Call<List<FinanceGetResponse>>>() {
    override suspend fun execute(param: String): Flow<Call<List<FinanceGetResponse>>> {
        return investmentRepository.getInvestmentsByUserId(param)
    }
}