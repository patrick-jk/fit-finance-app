package com.fitfinance.app.presentation.ui.financedetails

import android.util.Log
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinanceDetailsViewModel(
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
                it.enqueue(object : Callback<FinancePostResponse> {
                    override fun onResponse(p0: Call<FinancePostResponse>, p1: Response<FinancePostResponse>) {
                        if (p1.isSuccessful) {
                            _financePostResponse.value = State.Success(p1.body()!!)
                        } else {
                            _financePostResponse.value = State.Error(Throwable("Error creating finance"))
                        }
                    }

                    override fun onFailure(p0: Call<FinancePostResponse>, p1: Throwable) {
                        _financePostResponse.value = State.Error(p1)
                        Log.i("FinanceDashboardViewModel", p1.message.toString())
                    }
                })
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
                it.enqueue(object : Callback<Void> {
                    override fun onResponse(p0: Call<Void>, p1: Response<Void>) {
                        if (p1.isSuccessful) {
                            _financePutLiveData.value = State.Success(true)
                        } else {
                            _financePutLiveData.value = State.Error(Throwable("Error updating finance"))
                        }
                    }

                    override fun onFailure(p0: Call<Void>, p1: Throwable) {
                        _financePutLiveData.value = State.Error(p1)
                        Log.i("FinanceDashboardViewModel", p1.message.toString())
                    }
                })
            }
    }
}