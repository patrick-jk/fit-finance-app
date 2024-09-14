package com.fitfinance.app.presentation.ui.financedashboard

import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.usecase.finances.GetFinancesByUserIdUseCase
import com.fitfinance.app.domain.usecase.investments.GetInvestmentSummaryUseCase

class FinanceDashboardViewModel(
    private val financesByUserIdUseCase: GetFinancesByUserIdUseCase
) : ViewModel() {

}