package com.fitfinance.app.presentation.ui.investmentdashboard

import android.util.Log
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                it.enqueue(object : Callback<List<InvestmentGetResponse>> {
                    override fun onResponse(p0: Call<List<InvestmentGetResponse>>, p1: Response<List<InvestmentGetResponse>>) {
                        if (p1.isSuccessful) {
                            _investmentsList.value = State.Success(p1.body()!!)
                        } else {
                            _investmentsList.value = State.Error(Throwable("Error getting investments"))
                        }
                    }

                    override fun onFailure(p0: Call<List<InvestmentGetResponse>>, p1: Throwable) {
                        _investmentsList.value = State.Error(p1)
                        Log.i("InvestmentDashboardViewModel", p1.message.toString())
                    }
                })
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
                it.enqueue(object : Callback<Unit> {
                    override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {
                        if (p1.isSuccessful) {
                            _investmentDeleteObserver.value = State.Success(true)
                        } else {
                            _investmentDeleteObserver.value = State.Error(Throwable("Error deleting investment"))
                        }
                    }

                    override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                        _investmentDeleteObserver.value = State.Error(p1)
                        Log.i("InvestmentDashboardViewModel", p1.message.toString())
                    }
                })
            }
    }
}