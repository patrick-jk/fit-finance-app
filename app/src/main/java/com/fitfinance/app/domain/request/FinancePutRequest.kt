package com.fitfinance.app.domain.request

import com.fitfinance.app.domain.model.FinanceType
import java.time.LocalDate

data class FinancePutRequest(
    val id: Long,
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    var startDate: LocalDate,
    var endDate: LocalDate
)