package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.dao.UserSessionCrossRefDao
import com.example.myapplication.database.entities.model.UserSessionCrossRef

class OfflineUserSessionRepository(private val userSessionDao: UserSessionCrossRefDao) : UserSessionRepository {
    override suspend fun insertUserSession(userSessionCrossRef: UserSessionCrossRef) = userSessionDao.insert(userSessionCrossRef)

    override suspend fun updateUserSession(userSessionCrossRef: UserSessionCrossRef) = userSessionDao.update(userSessionCrossRef)

    override suspend fun deleteUserSession(userSessionCrossRef: UserSessionCrossRef) = userSessionDao.delete(userSessionCrossRef)

    override suspend fun deleteUserSessions(userId: Int) = userSessionDao.deleteByUserUid(userId)
}