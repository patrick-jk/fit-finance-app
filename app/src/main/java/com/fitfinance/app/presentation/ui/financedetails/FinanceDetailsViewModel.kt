package com.fitfinance.app.presentation.ui.financedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.domain.response.FinancePostResponse
import com.fitfinance.app.domain.usecase.finances.CreateFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.UpdateFinanceUseCase
import com.fitfinance.app.presentation.statepattern.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceDetailsViewModel @Inject constructor(
    private val createFinanceUseCase: CreateFinanceUseCase,
    private val updateFinanceUseCase: UpdateFinanceUseCase,
) : ViewModel() {
    private val _financePostResponse = MutableLiveData<State<FinancePostResponse>>()
    val financePostResponse: LiveData<State<FinancePostResponse>> = _financePostResponse

    private val _financePutLiveData = MutableLiveData<State<Boolean>>()
    val financePutLiveData: LiveData<State<Boolean>> = _financePutLiveData

    fun createFinance(financePostRequest: FinancePostRequest, jwtToken: String) = viewModelScope.launch {
        createFinanceUseCase(Pair(financePostRequest, jwtToken))
            .onStart {
                _financePostResponse.value = State.Loading("Creating finance...")
            }
            .catch {
                _financePostResponse.value = State.Error(it)
            }
            .collect {
                _financePostResponse.value = State.Success(it)
            }
    }

    fun updateFinance(financePutRequest: FinancePutRequest, jwtToken: String) = viewModelScope.launch {
        updateFinanceUseCase(Pair(financePutRequest, jwtToken))
            .onStart {
                _financePutLiveData.value = State.Loading("Updating finance...")
            }
            .catch {
                _financePutLiveData.value = State.Error(it)
            }
            .collect {
                _financePutLiveData.value = State.Success(true)
            }
    }
}