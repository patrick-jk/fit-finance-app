package com.fitfinance.app.domain.usecase.user

import com.fitfinance.app.data.repo.UserRepository
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<Pair<UserPutRequest, String>, Unit>() {
    override fun execute(param: Pair<UserPutRequest, String>): Flow<Unit> {
        return userRepository.updateUser(param.first, param.second)
    }
}