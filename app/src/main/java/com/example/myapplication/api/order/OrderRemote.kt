package com.example.myapplication.api.order

import com.example.myapplication.api.session.SessionFromOrderRemote
import com.example.myapplication.database.entities.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderRemote(
    val id: Int = 0, val userId: Int = 0, var sessions: List<SessionFromOrderRemote> = emptyList()
)

fun OrderRemote.toOrder(): Order = Order(
    id, userId
)

fun Order.toOrderRemote(): OrderRemote = OrderRemote(
    uid, userId!!, sessions = emptyList()
)