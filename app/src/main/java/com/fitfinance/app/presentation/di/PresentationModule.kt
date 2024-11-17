package com.fitfinance.app.presentation.di

import com.fitfinance.app.presentation.ui.financedashboard.FinanceDashboardViewModel
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsViewModel
import com.fitfinance.app.presentation.ui.home.HomeViewModel
import com.fitfinance.app.presentation.ui.investmentdashboard.InvestmentDashboardViewModel
import com.fitfinance.app.presentation.ui.investmentdetails.InvestmentDetailsViewModel
import com.fitfinance.app.presentation.ui.login.LoginViewModel
import com.fitfinance.app.presentation.ui.photogallery.PhotoGalleryViewModel
import com.fitfinance.app.presentation.ui.profile.ProfileViewModel
import com.fitfinance.app.presentation.ui.profile.changepassword.ChangePasswordViewModel
import com.fitfinance.app.presentation.ui.profile.edituser.EditUserProfileViewModel
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
        viewModel { ProfileViewModel(get()) }
        viewModel { PhotoGalleryViewModel() }
        viewModel { FinanceDashboardViewModel(get(), get()) }
        viewModel { FinanceDetailsViewModel(get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { InvestmentDashboardViewModel(get(), get()) }
        viewModel { InvestmentDetailsViewModel(get(), get()) }
        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegisterViewModel(get()) }
        viewModel { ProfileViewModel(get()) }
        viewModel { EditUserProfileViewModel(get()) }
        viewModel { ChangePasswordViewModel(get()) }
    }
}