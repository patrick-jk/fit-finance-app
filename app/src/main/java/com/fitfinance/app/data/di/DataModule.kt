package com.fitfinance.app.data.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.fitfinance.app.data.local.AppDatabase
import com.fitfinance.app.data.remote.ApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun providesApiService(): ApiService {
        val interceptor = HttpLoggingInterceptor {
            Log.i("OK_HTTP", it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gsonConverterFactory = GsonConverterFactory.create(GsonBuilder().create())

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build().create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesLocalDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "fit-finance-app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Singleton
    @Provides
    fun providesFinanceDao(appDatabase: AppDatabase) = appDatabase.financeDao()

    @Singleton
    @Provides
    fun providesInvestmentDao(appDatabase: AppDatabase) = appDatabase.investmentDao()

    @Singleton
    @Provides
    fun providesHomeSummaryDao(appDatabase: AppDatabase) = appDatabase.homeSummaryDao()
}