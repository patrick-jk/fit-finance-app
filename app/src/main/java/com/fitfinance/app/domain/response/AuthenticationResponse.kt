package com.fitfinance.app.domain.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    @SerializedName("access_token") var accessToken: String,
    @SerializedName("refresh_token") var refreshToken: String,
)
