package com.fitfinance.app.presentation.ui.financelist

import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.usecase.finances.GetFinancesByUserIdUseCase

class FinanceListViewModel(
    private val getFinancesByUserIdUseCase: GetFinancesByUserIdUseCase
) : ViewModel() {

}