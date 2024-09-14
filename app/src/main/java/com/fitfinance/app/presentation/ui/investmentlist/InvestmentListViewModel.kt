package com.fitfinance.app.presentation.ui.investmentlist

import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.usecase.investments.GetInvestmentsByUserIdUseCase

class InvestmentListViewModel(
    private val getInvestmentsByUserIdUseCase: GetInvestmentsByUserIdUseCase
) : ViewModel() {

}