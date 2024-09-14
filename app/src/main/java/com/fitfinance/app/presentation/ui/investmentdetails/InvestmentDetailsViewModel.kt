package com.fitfinance.app.presentation.ui.investmentdetails

import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.usecase.investments.CreateInvestmentUseCase
import com.fitfinance.app.domain.usecase.investments.DeleteInvestmentUseCase
import com.fitfinance.app.domain.usecase.investments.UpdateInvestmentUseCase

class InvestmentDetailsViewModel(
    private val createInvestmentUseCase: CreateInvestmentUseCase,
    private val updateInvestmentUseCase: UpdateInvestmentUseCase,
    private val deleteInvestmentUseCase: DeleteInvestmentUseCase
) : ViewModel() {

}