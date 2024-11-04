package com.fitfinance.app.presentation.ui.investmentdetails

import android.util.Log
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvestmentDetailsViewModel(
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
                it.enqueue(object : Callback<InvestmentPostResponse> {
                    override fun onResponse(p0: Call<InvestmentPostResponse>, p1: Response<InvestmentPostResponse>) {
                        if (p1.isSuccessful) {
                            _investmentPostResponse.value = State.Success(p1.body()!!)
                        } else {
                            _investmentPostResponse.value = State.Error(Throwable("Error creating investment"))
                        }
                    }

                    override fun onFailure(p0: Call<InvestmentPostResponse>, p1: Throwable) {
                        _investmentPostResponse.value = State.Error(p1)
                        Log.i("InvestmentDetailsViewModel", "Error fetching InvestmentPostResponse: ${p1.message.toString()}")
                    }
                })
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
                it.enqueue(object : Callback<Unit> {
                    override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {
                        if (p1.isSuccessful) {
                            _investmentPutResponse.value = State.Success(true)
                        } else {
                            _investmentPutResponse.value = State.Error(Throwable("Error updating investment"))
                        }
                    }

                    override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                        _investmentPutResponse.value = State.Error(p1)
                        Log.i("InvestmentDetailsViewModel", "Error fetching InvestmentPutResponse: ${p1.message.toString()}")
                    }
                })
            }
    }
}