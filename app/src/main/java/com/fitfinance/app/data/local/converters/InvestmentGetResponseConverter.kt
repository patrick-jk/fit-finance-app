package com.fitfinance.app.data.local.converters

import androidx.room.TypeConverter
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.google.gson.Gson

class InvestmentGetResponseConverter {
    @TypeConverter
    fun fromInvestmentGetResponse(investmentGetResponse: InvestmentGetResponse): String = Gson().toJson(investmentGetResponse)

    @TypeConverter
    fun toInvestmentGetResponse(investmentGetResponse: String): InvestmentGetResponse {
        return Gson().fromJson(investmentGetResponse, InvestmentGetResponse::class.java)
    }
}