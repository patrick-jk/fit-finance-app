package com.fitfinance.app.domain.di

import com.fitfinance.app.domain.usecase.auth.AuthenticateUserUseCase
import com.fitfinance.app.domain.usecase.auth.RefreshTokenUseCase
import com.fitfinance.app.domain.usecase.auth.RegisterUserUseCase
import com.fitfinance.app.domain.usecase.finances.CreateFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.DeleteFinanceUseCase
import com.fitfinance.app.domain.usecase.finances.GetFinancesByUserIdUseCase
import com.fitfinance.app.domain.usecase.finances.GetUserSummaryUseCase
import com.fitfinance.app.domain.usecase.finances.UpdateFinanceUseCase
import com.fitfinance.app.domain.usecase.investments.CreateInvestmentUseCase
import com.fitfinance.app.domain.usecase.investments.DeleteInvestmentUseCase
import com.fitfinance.app.domain.usecase.investments.GetInvestmentSummaryUseCase
import com.fitfinance.app.domain.usecase.investments.GetInvestmentsByUserIdUseCase
import com.fitfinance.app.domain.usecase.investments.UpdateInvestmentUseCase
import com.fitfinance.app.domain.usecase.user.UpdatePasswordUseCase
import com.fitfinance.app.domain.usecase.user.UpdateUserUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {
    fun load() {
        loadKoinModules(useCaseModule())
    }

    private fun useCaseModule(): Module = module {
        factory { RegisterUserUseCase(get()) }
        factory { AuthenticateUserUseCase(get()) }
        factory { RefreshTokenUseCase(get()) }

        factory { CreateFinanceUseCase(get()) }
        factory { DeleteFinanceUseCase(get()) }
        factory { GetFinancesByUserIdUseCase(get()) }
        factory { GetUserSummaryUseCase(get()) }
        factory { UpdateFinanceUseCase(get()) }

        factory { CreateInvestmentUseCase(get()) }
        factory { DeleteInvestmentUseCase(get()) }
        factory { GetInvestmentsByUserIdUseCase(get()) }
        factory { GetInvestmentSummaryUseCase(get()) }
        factory { UpdateInvestmentUseCase(get()) }

        factory { UpdateUserUseCase(get()) }
        factory { UpdatePasswordUseCase(get()) }
    }
}