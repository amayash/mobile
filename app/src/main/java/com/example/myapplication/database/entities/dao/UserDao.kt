package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("select * from users order by login collate nocase asc")
    fun getAll(): Flow<List<User>>

    @Query(
        "SELECT sessions.*, sessions.max_count-IFNULL(SUM(orders_sessions.count), 0) as available_count, " +
                "users_sessions.count FROM sessions " +
                "join users_sessions on sessions.uid = users_sessions.session_id " +
                "left join orders_sessions on sessions.uid = orders_sessions.session_id " +
                "where users_sessions.user_id = :userId " +
                "GROUP BY users_sessions.session_id "
    )
    fun getCartByUid(userId: Int): Flow<List<SessionFromCart>>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}