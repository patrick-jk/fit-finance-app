package com.fitfinance.app.domain.response

import com.fitfinance.app.domain.model.InvestmentType
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class InvestmentPostResponse(
    val id: Long,
    var name: String,
    var price: Double,
    var type: InvestmentType,
    var quantity: Int,
    var description: String,
    @SerializedName("start_date") var startDate: LocalDate,
    @SerializedName("end_date") var endDate: LocalDate?
)
