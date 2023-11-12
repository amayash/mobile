package com.example.myapplication.database.entities.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.myapplication.database.entities.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSession(uid: Int): Flow<Session?>
    suspend fun insertSession(session: Session)
    suspend fun updateSession(session: Session)
    suspend fun deleteSession(session: Session)
}