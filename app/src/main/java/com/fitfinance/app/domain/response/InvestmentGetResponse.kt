package com.fitfinance.app.domain.response

import android.os.Parcelable
import com.fitfinance.app.domain.model.InvestmentType
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvestmentGetResponse(
    val id: Long,
    var name: String,
    var price: Double,
    var type: InvestmentType,
    var quantity: Int,
    @SerializedName("start_date") var startDate: String,
    @SerializedName("end_date") var endDate: String?
) : Parcelable
