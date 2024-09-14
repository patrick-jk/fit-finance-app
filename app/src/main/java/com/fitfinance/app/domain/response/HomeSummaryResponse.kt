package com.fitfinance.app.domain.response

import java.math.BigDecimal

data class HomeSummaryResponse(
    val balance: BigDecimal,
    val totalExpenses: BigDecimal,
    val biggestExpense: FinanceGetResponse,
    val smallestExpense: FinanceGetResponse,
    val biggestInvestment: InvestmentGetResponse,
    val smallestInvestment: InvestmentGetResponse
)