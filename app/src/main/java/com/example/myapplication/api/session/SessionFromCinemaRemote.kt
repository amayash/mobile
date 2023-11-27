package com.example.myapplication.api.session

import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCinema
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime

@Serializable
class SessionFromCinemaRemote(
    val id: Int = 0,
    @Contextual
    val dateTime: LocalDateTime = LocalDateTime.MIN,
    val price: Double = 0.0,
    val availableCount: Int = 0,
    val cinemaId: Int = 0,
)

fun SessionFromCinemaRemote.toSessionFromCinema(): SessionFromCinema = SessionFromCinema(
    id,
    dateTime,
    price,
    availableCount,
    cinemaId
)

fun SessionFromCinema.toSessionFromCinemaRemote(): SessionFromCinemaRemote = SessionFromCinemaRemote(
    uid,
    dateTime,
    price,
    availableCount,
    cinemaId
)