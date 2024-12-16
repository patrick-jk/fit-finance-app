package com.fitfinance.app.domain.usecase.finances

import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.response.FinancePostResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateFinanceUseCase @Inject constructor(
    private val financeRepository: FinanceRepository
) : UseCase<Pair<FinancePostRequest, String>, FinancePostResponse>() {
    override fun execute(param: Pair<FinancePostRequest, String>): Flow<FinancePostResponse> {
        return financeRepository.createFinance(param.first, param.second)
    }
}