package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.domain.response.UserPostResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class RegisterUserUseCase(private val authRepository: AuthRepository) : UseCase<RegisterRequest, UserPostResponse>() {
    override suspend fun execute(param: RegisterRequest): Flow<UserPostResponse> {
        return authRepository.registerUser(param)
    }
}