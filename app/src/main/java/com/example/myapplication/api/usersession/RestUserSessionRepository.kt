package com.example.myapplication.api.usersession

import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.session.SessionFromCartRemote
import com.example.myapplication.api.session.toSession
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.OfflineUserSessionRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository

class RestUserSessionRepository(
    private val service: MyServerService,
    private val dbUserSessionRepository: OfflineUserSessionRepository
) : UserSessionRepository {
    override suspend fun insertUserSession(userSessionCrossRef: UserSessionCrossRef) {
        var cartSessions = service.getUserCart(userSessionCrossRef.userId)
        cartSessions.sessions.forEach { session ->
            if (session.id == userSessionCrossRef.sessionId)
                return
        }
        val session = service.getSession(userSessionCrossRef.sessionId).toSession()

        val sessionFromCart = SessionFromCartRemote(
            session.uid,
            userSessionCrossRef.count,
            session.cinemaId,
        )

        val updatedSessions = cartSessions.sessions.toMutableList()
        updatedSessions.add(sessionFromCart)

        cartSessions = cartSessions.copy(sessions = updatedSessions)
        service.updateUserCart(userSessionCrossRef.userId, cartSessions)
        dbUserSessionRepository.insertUserSession(userSessionCrossRef)
    }

    override suspend fun updateUserSession(userSessionCrossRef: UserSessionCrossRef) {
        val userRemote = service.getUserCart(userSessionCrossRef.userId)
        if (userSessionCrossRef.count <= 0) {
            userRemote.sessions =
                userRemote.sessions.filter { x -> x.id != userSessionCrossRef.sessionId }
        } else
            userRemote.sessions.forEach {
                if (it.id == userSessionCrossRef.sessionId) {
                    it.count = userSessionCrossRef.count
                }
            }
        service.updateUserCart(userSessionCrossRef.userId, userRemote)
        dbUserSessionRepository.updateUserSession(userSessionCrossRef)
    }

    override suspend fun deleteUserSession(userSessionCrossRef: UserSessionCrossRef) {
        updateUserSession(userSessionCrossRef)
        dbUserSessionRepository.deleteUserSession(userSessionCrossRef)
    }

    override suspend fun deleteUserSessions(userId: Int) {
        val userRemote = service.getUserCart(userId)
        userRemote.sessions = emptyList()
        service.updateUserCart(userId, userRemote)
        dbUserSessionRepository.deleteUserSessions(userId)
    }
}