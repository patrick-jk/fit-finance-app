package com.fitfinance.app.domain.response

import java.math.BigDecimal

data class InvestmentSummaryResponse(
    val totalStocks: BigDecimal,
    val totalFIIs: BigDecimal,
    val totalFixedIncome: BigDecimal,
)
