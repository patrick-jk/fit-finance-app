package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class UpdateUserUseCase(private val userRepository: UserRepository) : UseCase<Pair<UserPutRequest, String>, Call<Unit>>() {
    override suspend fun execute(param: Pair<UserPutRequest, String>): Flow<Call<Unit>> {
        return userRepository.updateUser(param.first, param.second)
    }
}