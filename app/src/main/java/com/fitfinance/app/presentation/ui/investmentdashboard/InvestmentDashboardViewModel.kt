package com.fitfinance.app.presentation.ui.investmentdashboard

import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.usecase.investments.GetInvestmentSummaryUseCase

class InvestmentDashboardViewModel(
    private val investmentSummaryUseCase: GetInvestmentSummaryUseCase
) : ViewModel() {

}