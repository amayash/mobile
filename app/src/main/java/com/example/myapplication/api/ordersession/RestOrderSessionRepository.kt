package com.example.myapplication.api.ordersession

import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.session.SessionFromOrderRemote
import com.example.myapplication.api.session.toSession
import com.example.myapplication.database.entities.model.OrderSessionCrossRef
import com.example.myapplication.database.entities.repository.OfflineOrderSessionRepository
import com.example.myapplication.database.entities.repository.OrderSessionRepository

class RestOrderSessionRepository(
    private val service: MyServerService,
    private val dbOrderSessionRepository: OfflineOrderSessionRepository
) : OrderSessionRepository {
    override suspend fun insertOrderSession(orderSessionCrossRef: OrderSessionCrossRef) {
        var orderRemote = service.getOrder(orderSessionCrossRef.orderId)
        val session = service.getSession(orderSessionCrossRef.sessionId).toSession()

        val sessionFromOrder = SessionFromOrderRemote(
            session.uid,
            session.dateTime,
            session.price,
            orderSessionCrossRef.count,
            session.cinemaId
        )

        val updatedSessions = orderRemote.sessions.toMutableList()
        updatedSessions.add(sessionFromOrder)

        orderRemote = orderRemote.copy(sessions = updatedSessions)
        service.updateOrder(orderSessionCrossRef.orderId, orderRemote)
        dbOrderSessionRepository.insertOrderSession(orderSessionCrossRef)
    }

    override suspend fun updateOrderSession(orderSessionCrossRef: OrderSessionCrossRef) {
    }

    override suspend fun deleteOrderSession(orderSessionCrossRef: OrderSessionCrossRef) {
    }
}