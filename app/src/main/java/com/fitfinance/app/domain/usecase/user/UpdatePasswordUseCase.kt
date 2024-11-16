package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class UpdatePasswordUseCase(private val userRepository: UserRepository) : UseCase<Pair<ChangePasswordRequest, String>, Unit>() {
    override suspend fun execute(param: Pair<ChangePasswordRequest, String>): Flow<Unit> {
        return userRepository.updatePassword(param.first, param.second)
    }
}