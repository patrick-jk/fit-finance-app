package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.response.FinancePostResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class CreateFinanceUseCase(private val financeRepository: FinanceRepository): UseCase<Pair<FinancePostRequest, String>, Call<FinancePostResponse>>() {
    override suspend fun execute(param: Pair<FinancePostRequest, String>): Flow<Call<FinancePostResponse>> {
        return financeRepository.createFinance(param.first, param.second)
    }
}