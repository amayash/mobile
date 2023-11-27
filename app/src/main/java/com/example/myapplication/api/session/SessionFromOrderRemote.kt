package com.example.myapplication.api.session

import com.example.myapplication.api.cinema.CinemaRemote
import com.example.myapplication.api.cinema.toCinema
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime

@Serializable
class SessionFromOrderRemote(
    val id: Int = 0,
    @Contextual val dateTime: LocalDateTime = LocalDateTime.MIN,
    val frozenPrice: Double = 0.0,
    val count: Int = 0,
    val cinemaId: Int = 0,
)

fun SessionFromOrderRemote.toSessionFromOrder(cinema: CinemaRemote): SessionFromOrder =
    SessionFromOrder(
        id, dateTime, frozenPrice, count, cinemaId, cinema.toCinema()
    )