package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class GetFinancesByUserIdUseCase(private val financeRepository: FinanceRepository): UseCase<String, Call<List<FinanceGetResponse>>>() {
    override suspend fun execute(param: String): Flow<Call<List<FinanceGetResponse>>> {
        return financeRepository.getFinancesByUserId(param)
    }
}