package com.fitfinance.app.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.usecase.auth.AuthenticateUserUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    fun authenticateUser(email: String, password: String) = viewModelScope.launch {
        authenticateUserUseCase(AuthenticationRequest(email, password))
            .collect {
                //TODO: Implement the logic to handle the response, if success, save the token in the shared preferences
            }
    }
}