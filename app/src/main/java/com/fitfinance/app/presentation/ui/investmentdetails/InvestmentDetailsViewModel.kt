package com.fitfinance.app.presentation.ui.investmentdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.domain.response.InvestmentPostResponse
import com.fitfinance.app.domain.usecase.investments.CreateInvestmentUseCase
import com.fitfinance.app.domain.usecase.investments.UpdateInvestmentUseCase
import com.fitfinance.app.presentation.statepattern.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvestmentDetailsViewModel @Inject constructor(
    private val createInvestmentUseCase: CreateInvestmentUseCase,
    private val updateInvestmentUseCase: UpdateInvestmentUseCase
) : ViewModel() {
    private val _investmentPostResponse = MutableLiveData<State<InvestmentPostResponse>>()
    val investmentPostResponse: LiveData<State<InvestmentPostResponse>> = _investmentPostResponse

    private val _investmentPutResponse = MutableLiveData<State<Boolean>>()
    val investmentPutResponse: LiveData<State<Boolean>> = _investmentPutResponse

    fun createInvestment(investmentPostResponse: InvestmentPostRequest, jwtToken: String) = viewModelScope.launch {
        createInvestmentUseCase(Pair(investmentPostResponse, jwtToken))
            .onStart {
                _investmentPostResponse.value = State.Loading("Creating investment...")
            }
            .catch {
                _investmentPostResponse.value = State.Error(it)
            }
            .collect {
                _investmentPostResponse.value = State.Success(it)
            }
    }

    fun updateInvestment(investmentPutRequest: InvestmentPutRequest, jwtToken: String) = viewModelScope.launch {
        updateInvestmentUseCase(Pair(investmentPutRequest, jwtToken))
            .onStart {
                _investmentPutResponse.value = State.Loading("Updating investment...")
            }
            .catch {
                _investmentPutResponse.value = State.Error(it)
            }
            .collect {
                _investmentPutResponse.value = State.Success(true)
            }
    }
}