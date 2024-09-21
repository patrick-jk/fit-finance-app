package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class RefreshTokenUseCase(private val authRepository: AuthRepository) : UseCase<String, Call<AuthenticationResponse>>() {
    override suspend fun execute(param: String): Flow<Call<AuthenticationResponse>> {
        return authRepository.refreshToken(param)
    }
}