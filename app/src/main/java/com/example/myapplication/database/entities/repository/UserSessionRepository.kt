package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.model.UserSessionCrossRef

interface UserSessionRepository {
    suspend fun insertUserSession(userSessionCrossRef: UserSessionCrossRef)
    suspend fun updateUserSession(userSessionCrossRef: UserSessionCrossRef)
    suspend fun deleteUserSession(userSessionCrossRef: UserSessionCrossRef)
    suspend fun deleteUserSessions(userId: Int)
}