package com.fitfinance.app.presentation.ui.investmentdashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.domain.usecase.investments.DeleteInvestmentUseCase
import com.fitfinance.app.domain.usecase.investments.GetInvestmentsByUserIdUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class InvestmentDashboardViewModel(
    private val investmentsByUserIdUseCase: GetInvestmentsByUserIdUseCase,
    private val deleteInvestmentUseCase: DeleteInvestmentUseCase
) : ViewModel() {
    private val _investmentsList = MutableLiveData<State<List<InvestmentGetResponse>>>()
    val investmentsList: LiveData<State<List<InvestmentGetResponse>>> = _investmentsList

    private val _investmentDeleteObserver = MutableLiveData<State<Boolean>>()
    val investmentDeleteObserver: LiveData<State<Boolean>> = _investmentDeleteObserver

    fun getInvestmentsByUserId(userId: String) = viewModelScope.launch {
        investmentsByUserIdUseCase(userId)
            .onStart {
                _investmentsList.value = State.Loading("Loading investments...")
            }
            .catch {
                _investmentsList.value = State.Error(it)
            }
            .collect {
                _investmentsList.value = State.Success(it)
            }
    }

    fun deleteInvestment(id: Long, jwtToken: String) = viewModelScope.launch {
        deleteInvestmentUseCase(Pair(id, jwtToken))
            .onStart {
                _investmentDeleteObserver.value = State.Loading("Deleting investment...")
            }
            .catch {
                _investmentDeleteObserver.value = State.Error(it)
            }
            .collect {
                _investmentDeleteObserver.value = State.Success(true)
            }
    }
}