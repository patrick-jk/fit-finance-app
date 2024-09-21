package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.response.HomeSummaryResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class GetUserSummaryUseCase(private val financeRepository: FinanceRepository): UseCase<String, Call<HomeSummaryResponse>>() {
    override suspend fun execute(param: String): Flow<Call<HomeSummaryResponse>> {
        return financeRepository.getUserSummary(param)
    }
}