package com.fitfinance.app.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.response.UserGetResponse
import com.fitfinance.app.domain.usecase.user.GetUserInfoUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfileViewModel(private val getUserInfoUseCase: GetUserInfoUseCase) : ViewModel() {
    private val _userGetResponse = MutableLiveData<State<UserGetResponse>>()
    val userGetResponse: LiveData<State<UserGetResponse>> = _userGetResponse

    fun getUserInfo(apiToken: String) = viewModelScope.launch {
        getUserInfoUseCase(apiToken).onStart {
            _userGetResponse.value = State.Loading("")
        }.catch {
            _userGetResponse.value = State.Error(it)
        }.collect {
            _userGetResponse.value = State.Success(it)
        }
    }
}