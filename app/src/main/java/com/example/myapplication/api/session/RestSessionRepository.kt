package com.example.myapplication.api.session

import com.example.myapplication.api.MyServerService
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.repository.OfflineOrderSessionRepository
import com.example.myapplication.database.entities.repository.OfflineSessionRepository
import com.example.myapplication.database.entities.repository.OfflineUserSessionRepository
import com.example.myapplication.database.entities.repository.SessionRepository

class RestSessionRepository(
    private val service: MyServerService,
    private val dbSessionRepository: OfflineSessionRepository,
    private val dbUserSessionRepository: OfflineUserSessionRepository,
    private val dbOrderSessionRepository: OfflineOrderSessionRepository,
) : SessionRepository {
    override suspend fun getSession(uid: Int): Session {
        return service.getSession(uid).toSession()
    }

    override suspend fun insertSession(session: Session) {
        dbSessionRepository.insertSession(
            service.createSession(session.toSessionRemote()).toSession()
        )
    }

    override suspend fun updateSession(session: Session) {
        dbSessionRepository.updateSession(
            service.updateSession(
                session.uid,
                session.toSessionRemote()
            ).toSession()
        )
    }

    override suspend fun deleteSession(session: Session) {
        val cart = service.getUsers()
        cart.forEach { userRemote ->
            userRemote.sessions = userRemote.sessions.filter { x -> x.id != session.uid }
            service.updateUserCart(userRemote.id, userRemote)
        }
        val orders = service.getOrders()
        orders.forEach { orderRemote ->
            orderRemote.sessions = orderRemote.sessions.filter { x -> x.id != session.uid }
            service.updateOrder(orderRemote.id, orderRemote)
        }
        service.deleteSession(session.uid)
        dbUserSessionRepository.deleteSessionsByUid(session.uid)
        dbOrderSessionRepository.deleteSessionsByUid(session.uid)
        dbSessionRepository.deleteSession(session)
    }
}