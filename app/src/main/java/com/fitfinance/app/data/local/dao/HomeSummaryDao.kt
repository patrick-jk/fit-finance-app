package com.fitfinance.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitfinance.app.data.local.entity.HomeSummaryEntity

@Dao
interface HomeSummaryDao {
    @Query("SELECT * FROM home_summary")
    suspend fun getHomeSummary(): HomeSummaryEntity

    @Insert
    suspend fun insertHomeSummary(homeSummary: HomeSummaryEntity)

    @Query("DELETE FROM home_summary")
    suspend fun deleteHomeSummary()
}