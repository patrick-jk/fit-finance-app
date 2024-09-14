package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.request.RegisterRequest

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(registerRequest: RegisterRequest) = authRepository.registerUser(registerRequest)
}