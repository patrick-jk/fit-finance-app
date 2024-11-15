package com.fitfinance.app.domain.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class HomeSummaryResponse(
    val balance: BigDecimal,
    val totalExpenses: BigDecimal,
    val biggestExpense: FinanceGetResponse,
    val smallestExpense: FinanceGetResponse,
    val biggestInvestment: InvestmentGetResponse,
    val smallestInvestment: InvestmentGetResponse
) : Parcelable