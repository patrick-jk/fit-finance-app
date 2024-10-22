package com.fitfinance.app.domain.request

import com.fitfinance.app.domain.model.FinanceType

data class FinancePutRequest(
    val id: Long,
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    var startDate: String,
    var endDate: String
)