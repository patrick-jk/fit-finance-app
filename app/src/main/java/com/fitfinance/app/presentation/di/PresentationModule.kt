package com.fitfinance.app.presentation.di

import com.fitfinance.app.presentation.ui.aboutus.AboutUsViewModel
import com.fitfinance.app.presentation.ui.contact.ContactUsViewModel
import com.fitfinance.app.presentation.ui.financedashboard.FinanceDashboardViewModel
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsViewModel
import com.fitfinance.app.presentation.ui.financelist.FinanceListViewModel
import com.fitfinance.app.presentation.ui.home.HomeViewModel
import com.fitfinance.app.presentation.ui.investmentdetails.InvestmentDetailsViewModel
import com.fitfinance.app.presentation.ui.investmentlist.InvestmentListViewModel
import com.fitfinance.app.presentation.ui.login.LoginViewModel
import com.fitfinance.app.presentation.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {
    fun load() {
        loadKoinModules(viewModelModule())
    }

    private fun viewModelModule(): Module = module {
        viewModel { AboutUsViewModel() }
        viewModel { ContactUsViewModel() }
        viewModel { FinanceDashboardViewModel(get()) }
        viewModel { FinanceDetailsViewModel(get(), get(), get()) }
        viewModel { FinanceListViewModel(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { InvestmentDetailsViewModel(get(), get(), get()) }
        viewModel { InvestmentListViewModel(get()) }
        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegisterViewModel(get()) }
    }
}