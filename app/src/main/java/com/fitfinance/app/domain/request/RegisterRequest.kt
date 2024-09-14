package com.fitfinance.app.domain.request

data class RegisterRequest(
    var name: String,
    var cpf: String,
    var email: String,
    var password: String,
    var phone: String,
    var birthdate: String,
    var income: Double
)