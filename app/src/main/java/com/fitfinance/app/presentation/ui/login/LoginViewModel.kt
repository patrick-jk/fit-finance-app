package com.fitfinance.app.presentation.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.domain.usecase.auth.AuthenticateUserUseCase
import com.fitfinance.app.domain.usecase.auth.RefreshTokenUseCase
import com.fitfinance.app.presentation.statepattern.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : ViewModel() {
    private val _authenticationState = MutableLiveData<State<AuthenticationResponse>>()
    val authenticationState: LiveData<State<AuthenticationResponse>> = _authenticationState

    private val _refreshTokenState = MutableLiveData<State<AuthenticationResponse>>()
    val refreshTokenState: LiveData<State<AuthenticationResponse>> = _refreshTokenState

    fun authenticateUser(email: String, password: String) = viewModelScope.launch {
        Log.i("LoginViewModel", "authenticateUser")
        authenticateUserUseCase(AuthenticationRequest(email, password))
            .onStart {
                _authenticationState.value = State.Loading("Authenticating user...")
            }
            .catch {
                _authenticationState.value = State.Error(it)
            }
            .collect {
                _authenticationState.value = State.Success(it)
            }
    }

    fun refreshToken(token: String) = viewModelScope.launch {
        Log.i("LoginViewModel", "refreshToken")
        refreshTokenUseCase(token)
            .onStart {
                _refreshTokenState.value = State.Loading("")
            }
            .catch {
                _refreshTokenState.value = State.Error(it)
            }
            .collect {
                _refreshTokenState.value = State.Success(it)
            }
    }
}