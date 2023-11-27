package com.example.myapplication.api.usersession

import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.session.toSession
import com.example.myapplication.api.session.SessionFromCartRemote
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.OfflineUserSessionRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository

class RestUserSessionRepository(
    private val service: MyServerService,
    private val dbUserSessionRepository: OfflineUserSessionRepository,
) : UserSessionRepository {
    override suspend fun insertUserSession(userSessionCrossRef: UserSessionCrossRef) {
        val cartSessions = service.getUserCart(userSessionCrossRef.userId)
        val session = service.getSession(userSessionCrossRef.sessionId).toSession()
        //val availableCount = dbUserSessionRepository.
        cartSessions.sessions.plus(
            SessionFromCartRemote(
                session.uid,
                session.dateTime,
                session.price,
                session.maxCount,
                userSessionCrossRef.count,
                session.cinemaId
            )
        )
        service.updateUserCart(userSessionCrossRef.userId, cartSessions)
    }

    override suspend fun updateUserSession(userSessionCrossRef: UserSessionCrossRef) {
        val cartSessions = service.getUserCart(userSessionCrossRef.userId)
        cartSessions.sessions.forEach {
            if (it.id == userSessionCrossRef.sessionId) {
                it.count = userSessionCrossRef.count
            }
        }
        service.updateUserCart(userSessionCrossRef.userId, cartSessions)
    }

    override suspend fun deleteUserSession(userSessionCrossRef: UserSessionCrossRef) {
        val cartSessions = service.getUserCart(userSessionCrossRef.userId)
        cartSessions.sessions = emptyList()
        service.updateUserCart(userSessionCrossRef.userId, cartSessions)
    }

    override suspend fun deleteUserSessions(userId: Int) {
    }
}