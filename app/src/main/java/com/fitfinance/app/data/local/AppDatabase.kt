package com.fitfinance.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitfinance.app.data.local.converters.FinanceGetResponseConverter
import com.fitfinance.app.data.local.converters.InvestmentGetResponseConverter
import com.fitfinance.app.data.local.converters.UserGetResponseConverter
import com.fitfinance.app.data.local.dao.FinanceDao
import com.fitfinance.app.data.local.dao.HomeSummaryDao
import com.fitfinance.app.data.local.dao.InvestmentDao
import com.fitfinance.app.data.local.dao.UserDao
import com.fitfinance.app.data.local.entity.FinanceEntity
import com.fitfinance.app.data.local.entity.HomeSummaryEntity
import com.fitfinance.app.data.local.entity.InvestmentEntity
import com.fitfinance.app.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, FinanceEntity::class, InvestmentEntity::class, HomeSummaryEntity::class], version = 2, exportSchema = false)
@TypeConverters(UserGetResponseConverter::class, FinanceGetResponseConverter::class, InvestmentGetResponseConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun financeDao(): FinanceDao
    abstract fun investmentDao(): InvestmentDao
    abstract fun homeSummaryDao(): HomeSummaryDao
}