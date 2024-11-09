package com.fitfinance.app.presentation.ui.financedashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.domain.usecase.finances.DeleteFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.GetFinancesByUserIdUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FinanceDashboardViewModel(
    private val getFinancesByUserIdUseCase: GetFinancesByUserIdUseCase,
    private val deleteFinanceUseCase: DeleteFinanceUseCase
) : ViewModel() {
    private val _financesList = MutableLiveData<State<List<FinanceGetResponse>>>()
    val financesList: LiveData<State<List<FinanceGetResponse>>> = _financesList

    private val _financeDeleteObserver = MutableLiveData<State<Boolean>>()
    val financeDeleteObserver: LiveData<State<Boolean>> = _financeDeleteObserver

    fun getFinancesByUserId(userId: String) = viewModelScope.launch {
        getFinancesByUserIdUseCase(userId)
            .onStart {
                _financesList.value = State.Loading("Loading finances...")
            }
            .catch {
                _financesList.value = State.Error(it)
            }
            .collect {
                _financesList.value = State.Success(it)
            }
    }

    fun deleteFinance(financeId: Long, jwtToken: String) = viewModelScope.launch {
        deleteFinanceUseCase(Pair(financeId, jwtToken))
            .onStart {
                _financeDeleteObserver.value = State.Loading("Deleting finance...")
            }
            .catch {
                _financeDeleteObserver.value = State.Error(it)
            }
            .collect {
                _financeDeleteObserver.value = State.Success(true)
            }
    }
}