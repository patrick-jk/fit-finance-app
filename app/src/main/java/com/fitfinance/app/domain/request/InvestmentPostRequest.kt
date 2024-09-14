package com.fitfinance.app.domain.request

import com.fitfinance.app.domain.model.InvestmentType
import java.time.LocalDate

data class InvestmentPostRequest(
    var name: String,
    var price: Double,
    var type: InvestmentType,
    var quantity: Int,
    var description: String,
    var startDate: LocalDate,
    var endDate: LocalDate?
)
