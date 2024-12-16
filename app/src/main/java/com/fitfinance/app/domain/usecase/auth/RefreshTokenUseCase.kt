package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<String, AuthenticationResponse>() {
    override fun execute(param: String): Flow<AuthenticationResponse> {
        return authRepository.refreshToken(param)
    }
}