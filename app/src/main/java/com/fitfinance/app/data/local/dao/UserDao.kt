package com.fitfinance.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitfinance.app.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getUserInfo(): UserEntity

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}