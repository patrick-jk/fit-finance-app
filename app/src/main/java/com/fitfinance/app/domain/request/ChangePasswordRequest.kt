package com.fitfinance.app.domain.request

data class ChangePasswordRequest(
    var currentPassword: String,
    var newPassword: String,
    var confirmationPassword: String
)
