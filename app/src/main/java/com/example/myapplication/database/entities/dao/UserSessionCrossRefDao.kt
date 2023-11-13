package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionCrossRefDao {
    @Insert
    suspend fun insert(userSessionCrossRef: UserSessionCrossRef)

    @Update
    suspend fun update(userSessionCrossRef: UserSessionCrossRef)

    @Delete
    suspend fun delete(userSessionCrossRef: UserSessionCrossRef)

    @Query("DELETE FROM users_sessions where users_sessions.user_id = :userId")
    suspend fun deleteByUserUid(userId: Int)
}