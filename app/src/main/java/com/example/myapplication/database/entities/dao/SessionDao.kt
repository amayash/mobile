package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("select * from sessions where sessions.uid = :uid")
    suspend fun getByUid(uid: Int): Session

    @Insert
    suspend fun insert(vararg session: Session)

    @Update
    suspend fun update(session: Session)

    @Delete
    suspend fun delete(session: Session)

    @Query("DELETE FROM sessions")
    suspend fun deleteAll()
}