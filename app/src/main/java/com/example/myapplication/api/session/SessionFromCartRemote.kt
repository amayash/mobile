package com.example.myapplication.api.session

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.myapplication.api.cinema.CinemaRemote
import com.example.myapplication.api.cinema.toCinema
import com.example.myapplication.api.cinema.toCinemaRemote
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.SessionFromCart
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime

@Serializable
class SessionFromCartRemote(
    val id: Int = 0,
    @Contextual
    val dateTime: LocalDateTime = LocalDateTime.MIN,
    val price: Double = 0.0,
    val availableCount: Int = 0,
    var count: Int = 0,
    val cinemaId: Int = 0,
)

fun SessionFromCartRemote.toSessionFromCart(cinema: CinemaRemote): SessionFromCart = SessionFromCart(
    id,
    dateTime,
    price,
    availableCount,
    count,
    cinemaId,
    cinema.toCinema()
)

fun SessionFromCart.toSessionFromCartRemote(): SessionFromCartRemote = SessionFromCartRemote(
    uid,
    dateTime,
    price,
    availableCount,
    count,
    cinemaId
)