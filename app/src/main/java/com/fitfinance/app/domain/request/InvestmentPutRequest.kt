package com.fitfinance.app.domain.request

import com.fitfinance.app.domain.model.InvestmentType

data class InvestmentPutRequest(
    val id: Long,
    var name: String,
    var price: Double,
    var type: InvestmentType,
    var quantity: Int,
    var startDate: String,
    var endDate: String?
)
