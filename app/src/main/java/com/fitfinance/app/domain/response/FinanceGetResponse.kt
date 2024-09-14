package com.fitfinance.app.domain.response

import com.fitfinance.app.domain.model.FinanceType
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class FinanceGetResponse(
    val id: Long,
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    @SerializedName("start_date") var startDate: LocalDate,
    @SerializedName("end_date") var endDate: LocalDate,
    var userGetResponse: UserGetResponse
)