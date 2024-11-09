package com.fitfinance.app.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.domain.response.UserGetResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = "finance")
@Parcelize
data class FinanceEntity(
    @PrimaryKey val id: Long,
    var name: String,
    var value: Double,
    var type: FinanceType,
    var description: String,
    var userGetResponse: UserGetResponse,
    var startDate: String,
    var endDate: String
) : Parcelable {
    fun toFinanceGetResponse() = FinanceGetResponse(
        id = id,
        name = name,
        value = value,
        type = type,
        description = description,
        userGetResponse = userGetResponse,
        startDate = startDate,
        endDate = endDate
    )
}
