package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.response.UserGetResponse
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow

class GetUserInfoUseCase(private val userRepository: UserRepository) : UseCase<String, UserGetResponse>() {
    override suspend fun execute(param: String): Flow<UserGetResponse> {
        return userRepository.getUserInfo(param)
    }
}