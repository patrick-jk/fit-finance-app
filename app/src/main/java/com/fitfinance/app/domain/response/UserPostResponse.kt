package com.fitfinance.app.domain.response

data class UserPostResponse(
    val id: Long,
    var name: String,
    var email: String,
    var income: Double
)
