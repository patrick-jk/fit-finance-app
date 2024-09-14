package com.fitfinance.app.domain.request

import com.fitfinance.app.domain.model.FinanceType
import java.time.LocalDate

data class FinancePostRequest(
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    var startDate: LocalDate,
    var endDate: LocalDate
)