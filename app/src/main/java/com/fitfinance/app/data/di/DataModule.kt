package com.fitfinance.app.data.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.fitfinance.app.data.local.AppDatabase
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
import java.time.Duration

object DataModule {
    private const val OK_HTTP = "OkHttp"

    fun load() {
        loadKoinModules(apiModule() + repositoriesModule() + localDatabaseModule())
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
                    .callTimeout(Duration.ofSeconds(30))
                    .connectTimeout(Duration.ofSeconds(30))
                    .build()
            }
            single {
                createApiService<ApiService>(get(), get())
            }
        }
    }

    private fun localDatabaseModule(): Module {
        return module {
            single {
                createLocalDatabase(get())
            }

            single {
                get<AppDatabase>().userDao()
            }
            single {
                get<AppDatabase>().financeDao()
            }
            single {
                get<AppDatabase>().investmentDao()
            }
        }
    }

    private inline fun <reified T> createApiService(client: OkHttpClient, factory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
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
                UserRepository(get(), get())
            }
            single {
                FinanceRepository(get(), get())
            }
            single {
                InvestmentRepository(get(), get())
            }
        }
    }

    private fun createLocalDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "fit-finance-app.db")
            .build()
    }
}