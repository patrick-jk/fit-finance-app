package com.fitfinance.app.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.domain.response.HomeSummaryResponse
import com.fitfinance.app.domain.response.InvestmentGetResponse
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "home_summary")
data class HomeSummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val balance: Double,
    val totalExpenses: Double,
    val biggestExpense: FinanceGetResponse,
    val smallestExpense: FinanceGetResponse,
    val biggestInvestment: InvestmentGetResponse,
    val smallestInvestment: InvestmentGetResponse
) : Parcelable {
    fun toHomeSummaryResponse() = HomeSummaryResponse(
        balance = balance.toBigDecimal(),
        totalExpenses = totalExpenses.toBigDecimal(),
        biggestExpense = biggestExpense,
        smallestExpense = smallestExpense,
        biggestInvestment = biggestInvestment,
        smallestInvestment = smallestInvestment
    )
}