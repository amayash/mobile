package com.example.myapplication.api.session

import com.example.myapplication.api.cinema.CinemaRemote
import com.example.myapplication.api.cinema.toCinema
import com.example.myapplication.database.entities.model.SessionFromCart
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime

@Serializable
class SessionFromCartRemote(
    val id: Int = 0,
    var count: Int = 0,
    var cinemaId: Int = 0,
)

fun SessionFromCartRemote.toSessionFromCart(cinema: CinemaRemote, dateTime: LocalDateTime, price: Double, availableCount: Int): SessionFromCart =
    SessionFromCart(
        id, dateTime, price, availableCount, count, cinema.id, cinema.toCinema()
    )