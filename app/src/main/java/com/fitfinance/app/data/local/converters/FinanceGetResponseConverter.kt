package com.fitfinance.app.data.local.converters

import androidx.room.TypeConverter
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.google.gson.Gson

class FinanceGetResponseConverter {
    @TypeConverter
    fun fromFinanceGetResponse(financeGetResponse: FinanceGetResponse): String = Gson().toJson(financeGetResponse)

    @TypeConverter
    fun toFinanceGetResponse(financeGetResponse: String): FinanceGetResponse {
        return Gson().fromJson(financeGetResponse, FinanceGetResponse::class.java)
    }
}