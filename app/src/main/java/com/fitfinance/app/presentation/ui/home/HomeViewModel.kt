package com.fitfinance.app.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.response.HomeSummaryResponse
import com.fitfinance.app.domain.usecase.finances.GetUserSummaryUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeSummaryUseCase: GetUserSummaryUseCase
) : ViewModel() {
    private val _homeSummary = MutableLiveData<State<HomeSummaryResponse>>()
    val homeSummary: LiveData<State<HomeSummaryResponse>> = _homeSummary

    fun getHomeData(apiToken: String) = viewModelScope.launch {
        homeSummaryUseCase(apiToken)
            .onStart {
                _homeSummary.value = State.Loading("Loading home data...")
            }
            .catch {
                _homeSummary.value = State.Error(it)
            }
            .collect {
                _homeSummary.value = State.Success(it)
            }
    }
}