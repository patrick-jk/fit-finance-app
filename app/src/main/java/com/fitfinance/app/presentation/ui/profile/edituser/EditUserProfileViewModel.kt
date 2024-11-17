package com.fitfinance.app.presentation.ui.profile.edituser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.domain.usecase.user.UpdateUserUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EditUserProfileViewModel(private val updateUserUseCase: UpdateUserUseCase) : ViewModel() {
    private val _editUserProfileState = MutableLiveData<State<Boolean>>()
    val editUserProfileState: LiveData<State<Boolean>> = _editUserProfileState

    fun updateUser(apiToken: String, updatedUser: UserPutRequest) = viewModelScope.launch {
        updateUserUseCase(Pair(updatedUser, apiToken))
            .onStart {
                _editUserProfileState.value = State.Loading("")
            }
            .catch {
                _editUserProfileState.value = State.Error(it)
            }
            .collect {
                _editUserProfileState.value = State.Success(true)
            }
    }
}
