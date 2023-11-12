package com.example.myapplication.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.model.Cinema
import com.example.myapplication.entities.model.SessionFromCinema
import kotlinx.coroutines.flow.Flow

@Dao
interface CinemaDao {
    @Query("select * from cinemas order by name")
    fun getAll(): Flow<List<Cinema>>

    @Query("SELECT c.*, s.date_time, s.price, s.max_count-IFNULL(SUM(os.count), 0) as available_count " +
            "FROM cinemas AS c " +
            "JOIN sessions AS s ON s.cinema_id = c.uid " +
            "LEFT JOIN orders_sessions AS os ON os.session_id = s.uid " +
            "WHERE c.uid = :cinemaId " +
            "GROUP BY os.session_id")
    suspend fun getByUid(cinemaId: Int?): Map<Cinema, List<SessionFromCinema>>

    @Insert
    suspend fun insert(cinema: Cinema)

    @Update
    suspend fun update(cinema: Cinema)

    @Delete
    suspend fun delete(cinema: Cinema)
}