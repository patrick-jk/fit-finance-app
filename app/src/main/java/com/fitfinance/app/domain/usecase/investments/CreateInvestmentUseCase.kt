package com.fitfinance.app.domain.usecase.investments

import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.response.InvestmentPostResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateInvestmentUseCase @Inject constructor(
    private val investmentRepository: InvestmentRepository
) : UseCase<Pair<InvestmentPostRequest, String>, InvestmentPostResponse>() {
    override fun execute(param: Pair<InvestmentPostRequest, String>): Flow<InvestmentPostResponse> {
        return investmentRepository.createInvestment(param.first, param.second)
    }
}