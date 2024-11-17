package com.fitfinance.app.domain.response

import android.os.Parcelable
import com.fitfinance.app.data.local.entity.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserGetResponse(
    val id: Long,
    var name: String,
    var cpf: String,
    var email: String,
    var phone: String,
    var birthdate: String,
    var income: Double
) : Parcelable {
    fun toUserEntity() = UserEntity(
        id = id,
        name = name,
        cpf = cpf,
        email = email,
        phone = phone,
        birthdate = birthdate,
        income = income
    )
}
