package com.fitfinance.app.domain.response

data class UserGetResponse(
    val id: Long,
    var name: String,
    var cpf: String,
    var email: String,
    var phone: String,
    var birthdate: String,
    var income: Double
)
