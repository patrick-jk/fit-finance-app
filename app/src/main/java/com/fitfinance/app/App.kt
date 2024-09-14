package com.fitfinance.app

import android.app.Application
import com.fitfinance.app.data.di.DataModule
import com.fitfinance.app.domain.di.DomainModule
import com.fitfinance.app.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        DataModule.load()
        DomainModule.load()
        PresentationModule.load()
    }
}