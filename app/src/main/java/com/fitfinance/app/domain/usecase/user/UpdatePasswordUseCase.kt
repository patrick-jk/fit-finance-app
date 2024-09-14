package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.request.ChangePasswordRequest

class UpdatePasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(changePasswordRequest: ChangePasswordRequest, apiToken: String) = userRepository.updatePassword(changePasswordRequest, apiToken)
}