package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class GetFinancesByUserIdUseCase(private val financeRepository: FinanceRepository) : UseCase<String, List<FinanceGetResponse>>() {
    override suspend fun execute(param: String): Flow<List<FinanceGetResponse>> {
        return financeRepository.getFinancesByUserId(param)
    }
}