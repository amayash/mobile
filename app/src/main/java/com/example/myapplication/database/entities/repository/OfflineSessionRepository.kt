package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.dao.SessionDao
import com.example.myapplication.database.entities.model.Session
import kotlinx.coroutines.flow.Flow

class OfflineSessionRepository(private val sessionDao: SessionDao) : SessionRepository {
    override fun getSession(uid: Int): Flow<Session?> = sessionDao.getByUid(uid)

    override suspend fun insertSession(session: Session) = sessionDao.insert(session)

    override suspend fun updateSession(session: Session) = sessionDao.update(session)

    override suspend fun deleteSession(session: Session) = sessionDao.delete(session)
}