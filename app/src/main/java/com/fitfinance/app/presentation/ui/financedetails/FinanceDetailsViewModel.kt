package com.fitfinance.app.presentation.ui.financedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.usecase.finances.CreateFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.DeleteFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.UpdateFinanceUseCase

class FinanceDetailsViewModel(
    private val createFinanceUseCase: CreateFinanceUseCase,
    private val updateFinanceUseCase: UpdateFinanceUseCase,
    private val deleteFinanceUseCase: DeleteFinanceUseCase
) : ViewModel() {
}