package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class UpdatePasswordUseCase(private val userRepository: UserRepository): UseCase<Pair<ChangePasswordRequest, String>, Call<Unit>>() {
    override suspend fun execute(param: Pair<ChangePasswordRequest, String>): Flow<Call<Unit>> {
        return userRepository.updatePassword(param.first, param.second)
    }
}