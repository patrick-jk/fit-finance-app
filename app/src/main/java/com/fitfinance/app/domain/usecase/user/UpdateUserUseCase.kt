package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.request.UserPutRequest

class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userPutRequest: UserPutRequest, apiToken: String) = userRepository.updateUser(userPutRequest, apiToken)
}