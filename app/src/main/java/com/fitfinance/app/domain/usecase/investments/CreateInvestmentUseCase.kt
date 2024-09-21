package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.response.InvestmentPostResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class CreateInvestmentUseCase(private val investmentRepository: InvestmentRepository): UseCase<Pair<InvestmentPostRequest, String>, Call<InvestmentPostResponse>>() {
    override suspend fun execute(param: Pair<InvestmentPostRequest, String>): Flow<Call<InvestmentPostResponse>> {
        return investmentRepository.createInvestment(param.first, param.second)
    }
}