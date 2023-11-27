package com.example.myapplication.api.session

import com.example.myapplication.database.entities.model.Session
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime

@Serializable
data class SessionRemote(
    val id: Int = 0,
    @Contextual
    val dateTime: LocalDateTime,
    val price: Double,
    val maxCount: Int,
    val cinemaId: Int = 0,
)

fun SessionRemote.toSession(): Session = Session(
    id,
    dateTime,
    price,
    maxCount,
    cinemaId,
)

fun Session.toSessionRemote(): SessionRemote = SessionRemote(
    uid,
    dateTime,
    price,
    maxCount,
    cinemaId,
)