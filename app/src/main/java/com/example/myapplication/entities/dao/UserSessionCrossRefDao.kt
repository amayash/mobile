package com.example.myapplication.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.model.UserSessionCrossRef
import com.example.myapplication.entities.model.UserWithSession
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionCrossRefDao {
    @Query(
        "SELECT * FROM users_sessions " +
                "WHERE users_sessions.user_id = :userId"
    )
    fun getByUid(userId: Int): Flow<List<UserWithSession>>

    @Insert
    suspend fun insert(userSessionCrossRef: UserSessionCrossRef)

    @Update
    suspend fun update(userSessionCrossRef: UserSessionCrossRef)

    @Delete
    suspend fun delete(userSessionCrossRef: UserSessionCrossRef)
}