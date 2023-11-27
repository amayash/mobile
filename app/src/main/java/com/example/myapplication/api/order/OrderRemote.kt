package com.example.myapplication.api.order

import com.example.myapplication.api.session.SessionFromOrderRemote
import com.example.myapplication.api.session.toSessionFromOrder
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.serialization.Serializable

@Serializable
data class OrderRemote(
    val id: Int = 0,
    val userId: Int = 0,
    val sessions: List<SessionFromOrderRemote> = emptyList()
)

fun OrderRemote.toListSessionsFromOrder(): List<SessionFromOrder> =
    sessions.map { x -> x.toSessionFromOrder() }

fun OrderRemote.toOrder(): Order = Order(
    id,
    userId
)

fun Order.toOrderRemote(): OrderRemote = OrderRemote(
    uid,
    userId!!,
    sessions = emptyList()
)