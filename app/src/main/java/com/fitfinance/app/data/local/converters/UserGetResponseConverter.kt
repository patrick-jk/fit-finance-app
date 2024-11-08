package com.fitfinance.app.data.local.converters

import androidx.room.TypeConverter
import com.fitfinance.app.domain.response.UserGetResponse
import com.google.gson.Gson

class UserGetResponseConverter {
    @TypeConverter
    fun fromUserGetResponse(userGetResponse: UserGetResponse): String = Gson().toJson(userGetResponse)

    @TypeConverter
    fun toUserGetResponse(userGetResponse: String): UserGetResponse {
        return Gson().fromJson(userGetResponse, UserGetResponse::class.java)
    }
}