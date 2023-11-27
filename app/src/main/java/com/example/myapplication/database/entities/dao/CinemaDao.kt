package com.example.myapplication.database.entities.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.SessionFromCinema
import kotlinx.coroutines.flow.Flow

@Dao
interface CinemaDao {
    @Query("select * from cinemas order by name")
    fun getAll(): PagingSource<Int, Cinema>

    @Query(
        "SELECT c.*, s.uid as session_uid, s.date_time, s.price, s.max_count-IFNULL(SUM(os.count), 0) as available_count, c.uid as cinema_id " +
                "FROM cinemas AS c " +
                "LEFT JOIN sessions AS s ON s.cinema_id = c.uid " +
                "LEFT JOIN orders_sessions AS os ON os.session_id = s.uid " +
                "WHERE c.uid = :cinemaId " +
                "GROUP BY session_uid"
    )
    fun getByUid(cinemaId: Int?): Flow<Map<Cinema, List<SessionFromCinema>>>

    @Insert
    suspend fun insert(vararg cinema: Cinema)

    @Update
    suspend fun update(cinema: Cinema)

    @Delete
    suspend fun delete(cinema: Cinema)

    @Query("DELETE FROM cinemas")
    suspend fun deleteAll()
}