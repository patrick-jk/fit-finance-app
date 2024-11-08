package com.fitfinance.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fitfinance.app.data.local.entity.FinanceEntity

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance")
    suspend fun getFinances(): List<FinanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFinances(response: List<FinanceEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFinance(finance: FinanceEntity)

    @Delete
    suspend fun deleteFinance(finance: FinanceEntity)

    @Query("DELETE FROM finance")
    suspend fun deleteAllFinances()
}