package com.fitfinance.app.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.domain.response.UserPostResponse
import com.fitfinance.app.domain.usecase.auth.RegisterUserUseCase
import com.fitfinance.app.presentation.statepattern.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {
    private val _registerState = MutableLiveData<State<UserPostResponse>>()
    val registerState: LiveData<State<UserPostResponse>> = _registerState

    fun registerUser(registerRequest: RegisterRequest) = viewModelScope.launch {
        registerUserUseCase(registerRequest)
            .onStart {
                _registerState.value = State.Loading("Registering user...")
            }
            .catch {
                _registerState.value = State.Error(it)
            }
            .collect {
                _registerState.value = State.Success(it)
            }
    }
}