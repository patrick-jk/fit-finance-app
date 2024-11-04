package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class GetInvestmentsByUserIdUseCase(private val investmentRepository: InvestmentRepository) : UseCase<String, Call<List<InvestmentGetResponse>>>() {
    override suspend fun execute(param: String): Flow<Call<List<InvestmentGetResponse>>> {
        return investmentRepository.getInvestmentsByUserId(param)
    }
}