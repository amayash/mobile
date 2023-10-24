package com.example.myapplication.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("select * from users order by login collate nocase asc")
    fun getAll(): Flow<List<User>>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}