package com.fitfinance.app.presentation.ui.financedashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.domain.response.FinancePostResponse
import com.fitfinance.app.domain.usecase.finances.CreateFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.DeleteFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.GetFinancesByUserIdUseCase
import com.fitfinance.app.domain.usecase.finances.UpdateFinanceUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinanceDashboardViewModel(
    private val getFinancesByUserIdUseCase: GetFinancesByUserIdUseCase,
    private val createFinanceUseCase: CreateFinanceUseCase,
    private val updateFinanceUseCase: UpdateFinanceUseCase,
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
                it.enqueue(object : Callback<List<FinanceGetResponse>> {
                    override fun onResponse(p0: Call<List<FinanceGetResponse>>, p1: Response<List<FinanceGetResponse>>) {
                        if (p1.isSuccessful) {
                            _financesList.value = State.Success(p1.body()!!)
                        } else {
                            _financesList.value = State.Error(Throwable("Error getting finances"))
                        }
                    }

                    override fun onFailure(p0: Call<List<FinanceGetResponse>>, p1: Throwable) {
                        _financesList.value = State.Error(p1)
                        Log.i("FinanceDashboardViewModel", p1.message.toString())
                    }
                })
            }
    }

    fun deleteFinance(financeId: Long, jwtToken: String) = viewModelScope.launch {
        deleteFinanceUseCase(Pair(financeId, jwtToken))
            .onStart {
                _financesList.value = State.Loading("Deleting finance...")
            }
            .catch {
                _financesList.value = State.Error(it)
            }
            .collect {
                it.enqueue(object : Callback<Unit> {
                    override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {
                        if (p1.isSuccessful) {
                            _financeDeleteObserver.value = State.Success(true)
                        } else {
                            _financesList.value = State.Error(Throwable("Error deleting finance"))
                        }
                    }

                    override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                        _financesList.value = State.Error(p1)
                        Log.i("FinanceDashboardViewModel", p1.message.toString())
                    }
                })
            }
    }
}