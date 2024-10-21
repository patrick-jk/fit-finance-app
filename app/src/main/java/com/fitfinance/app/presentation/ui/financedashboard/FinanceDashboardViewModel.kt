package com.fitfinance.app.presentation.ui.financedashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.domain.usecase.finances.GetFinancesByUserIdUseCase
import com.fitfinance.app.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinanceDashboardViewModel(
    private val getFinancesByUserIdUseCase: GetFinancesByUserIdUseCase
) : ViewModel() {
    private val _financesList = MutableLiveData<State<List<FinanceGetResponse>>>()
    val financesList: LiveData<State<List<FinanceGetResponse>>> = _financesList

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
}