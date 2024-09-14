package com.fitfinance.app.domain.response

import com.fitfinance.app.domain.model.FinanceType
import java.time.LocalDate

data class FinancePostResponse(
    val id: Long,
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var userPostResponse: UserPostResponse
)