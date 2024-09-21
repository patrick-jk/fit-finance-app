package com.fitfinance.app.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.domain.response.UserPostResponse
import com.fitfinance.app.domain.usecase.auth.RegisterUserUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {
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
                it.enqueue(object : Callback<UserPostResponse> {
                    override fun onResponse(p0: Call<UserPostResponse>, p1: Response<UserPostResponse>) {
                        if (p1.code() == 201) {
                            _registerState.value = State.Success(p1.body()!!)
                        } else {
                            _registerState.value = State.Error(Throwable("Error registering user"))
                        }
                    }

                    override fun onFailure(p0: Call<UserPostResponse>, p1: Throwable) {
                        _registerState.value = State.Error(p1)
                    }

                })
            }
    }
}