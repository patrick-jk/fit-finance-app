package com.fitfinance.app.data.di

import android.util.Log
import com.fitfinance.app.data.remote.ApiService
import com.fitfinance.app.data.repo.AuthRepository
import com.fitfinance.app.data.repo.FinanceRepository
import com.fitfinance.app.data.repo.InvestmentRepository
import com.fitfinance.app.data.repo.UserRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {
    private const val OK_HTTP = "OkHttp"

    fun load() {
        loadKoinModules(apiModule() + repositoriesModule())
    }

    private fun apiModule(): Module {
        return module {
            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.i(OK_HTTP, it)
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
            single {
                createApiService<ApiService>(get(), get())
            }
        }
    }

    private inline fun <reified T> createApiService(client: OkHttpClient, factory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl("https://fit-finance-backend.onrender.com/api/v1/")
            .client(client)
            .addConverterFactory(factory)
            .build().create(T::class.java)
    }

    private fun repositoriesModule(): Module {
        return module {
            single {
                AuthRepository(get())
            }
            single {
                UserRepository(get())
            }
            single {
                FinanceRepository(get())
            }
            single {
                InvestmentRepository(get())
            }
        }
    }
}