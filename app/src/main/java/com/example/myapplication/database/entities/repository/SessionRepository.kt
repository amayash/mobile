package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun getSession(uid: Int): Session
    suspend fun insertSession(session: Session)
    suspend fun updateSession(session: Session)
    suspend fun deleteSession(session: Session)
}