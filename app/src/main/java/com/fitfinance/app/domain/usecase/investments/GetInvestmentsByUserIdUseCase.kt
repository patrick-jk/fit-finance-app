package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class GetInvestmentsByUserIdUseCase(private val investmentRepository: InvestmentRepository) : UseCase<String, List<InvestmentGetResponse>>() {
    override suspend fun execute(param: String): Flow<List<InvestmentGetResponse>> {
        return investmentRepository.getInvestmentsByUserId(param)
    }
}