package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class AuthenticateUserUseCase(private val authRepository: AuthRepository) : UseCase<AuthenticationRequest, Call<AuthenticationResponse>>() {
    override suspend fun execute(param: AuthenticationRequest): Flow<Call<AuthenticationResponse>> {
        return authRepository.authenticateUser(param)
    }
}