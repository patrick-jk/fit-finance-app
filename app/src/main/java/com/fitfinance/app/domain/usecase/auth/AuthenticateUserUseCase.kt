package com.fitfinance.app.domain.usecase.auth

import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<AuthenticationRequest, AuthenticationResponse>() {
    override fun execute(param: AuthenticationRequest): Flow<AuthenticationResponse> {
        return authRepository.authenticateUser(param)
    }
}