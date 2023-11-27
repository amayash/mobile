package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.UserSessionCrossRef

@Dao
interface UserSessionCrossRefDao {
    @Query(
        "SELECT s.max_count-IFNULL(SUM(os.count), 0) as available_count " +
                "FROM sessions AS s " +
                "LEFT JOIN orders_sessions AS os ON os.session_id = s.uid " +
                "WHERE s.uid = :sessionId " +
                "GROUP BY s.uid"
    )
    suspend fun getAvailableCountOfSessions(sessionId: Int): Int

    @Insert
    suspend fun insert(userSessionCrossRef: UserSessionCrossRef)

    @Update
    suspend fun update(userSessionCrossRef: UserSessionCrossRef)

    @Delete
    suspend fun delete(userSessionCrossRef: UserSessionCrossRef)

    @Query("DELETE FROM users_sessions where users_sessions.user_id = :userId")
    suspend fun deleteByUserUid(userId: Int)
}