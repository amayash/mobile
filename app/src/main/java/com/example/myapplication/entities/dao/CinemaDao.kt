package com.example.myapplication.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.model.Cinema
import com.example.myapplication.entities.model.CinemaWithSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface CinemaDao {
    @Query("select * from cinemas order by name")
    fun getAll(): Flow<List<Cinema>>

    @Query("select * from cinemas where uid = :uid")
    suspend fun getByUid(uid: Int): CinemaWithSessions

    @Insert
    suspend fun insert(cinema: Cinema)

    @Update
    suspend fun update(cinema: Cinema)

    @Delete
    suspend fun delete(cinema: Cinema)
}