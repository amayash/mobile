package com.example.myapplication.order.model

import com.example.myapplication.cinema.model.Cinema
import com.example.myapplication.cinema.model.getCinemas
import com.example.myapplication.session.model.Session
import com.example.myapplication.session.model.getSessions
import org.threeten.bp.LocalDateTime
import java.io.Serializable

data class Order(
    val sessions: List<Pair<Session, Int>>
) : Serializable

fun getOrders(): List<Order> {
    return listOf(
        Order(listOf(Pair(getSessions()[0], 5), Pair(getSessions()[1], 2))),
        Order(listOf(Pair(getSessions()[0], 1), Pair(getSessions()[1], 4))),
        Order(listOf(Pair(getSessions()[0], 1), Pair(getSessions()[1], 7)))
    )
}