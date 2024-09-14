package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.response.AuthenticationResponse
import kotlinx.coroutines.flow.Flow

class AuthenticateUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(authenticationRequest: AuthenticationRequest): Flow<Any> = authRepository.authenticateUser(authenticationRequest)
}