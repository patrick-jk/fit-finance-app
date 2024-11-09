package com.fitfinance.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fitfinance.app.data.local.entity.InvestmentEntity

@Dao
interface InvestmentDao {
    @Query("SELECT * FROM investment")
    suspend fun getInvestments(): List<InvestmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestment(investment: InvestmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInvestments(investments: List<InvestmentEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInvestment(investment: InvestmentEntity)

    @Delete
    suspend fun deleteInvestment(investment: InvestmentEntity)

    @Query("DELETE FROM investment")
    suspend fun deleteAllInvestments()
}