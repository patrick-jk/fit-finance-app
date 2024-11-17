package com.fitfinance.app.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitfinance.app.domain.response.UserGetResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class UserEntity(
    @PrimaryKey val id: Long,
    var name: String,
    var cpf: String,
    var email: String,
    var phone: String,
    var birthdate: String,
    var income: Double
) : Parcelable {
    fun toUserGetResponse(): UserGetResponse {
        return UserGetResponse(
            id = id,
            name = name,
            cpf = cpf,
            email = email,
            phone = phone,
            birthdate = birthdate,
            income = income
        )
    }
}
