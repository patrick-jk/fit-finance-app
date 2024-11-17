package com.fitfinance.app.presentation.ui.profile.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.domain.usecase.user.UpdatePasswordUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val updatePasswordUseCase: UpdatePasswordUseCase) : ViewModel() {
    private val _changePasswordState = MutableLiveData<State<Boolean>>()
    val changePasswordState: LiveData<State<Boolean>> = _changePasswordState

    fun updatePassword(apiToken: String, changePasswordRequest: ChangePasswordRequest) = viewModelScope.launch {
        updatePasswordUseCase(Pair(changePasswordRequest, apiToken))
            .onStart {
                _changePasswordState.value = State.Loading("")
            }
            .catch {
                _changePasswordState.value = State.Error(it)
            }
            .collect {
                _changePasswordState.value = State.Success(true)
            }
    }
}
