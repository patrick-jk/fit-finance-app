package com.fitfinance.app.domain.response

import android.os.Parcelable
import com.fitfinance.app.data.local.entity.HomeSummaryEntity
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
) : Parcelable {
    fun toHomeSummaryEntity() = HomeSummaryEntity(
        balance = balance.toDouble(),
        totalExpenses = totalExpenses.toDouble(),
        biggestExpense = biggestExpense,
        smallestExpense = smallestExpense,
        biggestInvestment = biggestInvestment,
        smallestInvestment = smallestInvestment
    )
}