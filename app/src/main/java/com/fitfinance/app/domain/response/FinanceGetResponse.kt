package com.fitfinance.app.domain.response

import android.os.Parcelable
import com.fitfinance.app.domain.model.FinanceType
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinanceGetResponse(
    val id: Long,
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    @SerializedName("user") var userGetResponse: UserGetResponse,
    @SerializedName("start_date") var startDate: String,
    @SerializedName("end_date") var endDate: String?
) : Parcelable