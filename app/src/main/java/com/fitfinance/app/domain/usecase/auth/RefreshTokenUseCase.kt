package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class RefreshTokenUseCase(private val authRepository: AuthRepository) : UseCase<String, AuthenticationResponse>() {
    override suspend fun execute(param: String): Flow<AuthenticationResponse> {
        return authRepository.refreshToken(param)
    }
}