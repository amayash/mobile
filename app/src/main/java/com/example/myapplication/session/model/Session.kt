package com.example.myapplication.session.model

import com.example.myapplication.cinema.model.Cinema
import com.example.myapplication.cinema.model.getCinemas
import java.io.Serializable
import org.threeten.bp.LocalDateTime;
import kotlin.streams.toList

data class Session(
    val dateTime: LocalDateTime,
    val cinema: Cinema,
    val currentCount: Int,
    val maxCount: Int
    )

fun getSessions(cinema: Cinema): List<Session> {
    return getSessions().stream().filter{ value -> value.cinema == cinema }.toList();
}

fun getSessions(): List<Session> {
    return listOf(
        Session(LocalDateTime.of(2023, 10, 10, 18, 30), getCinemas()[0], 50, 120),
        Session(LocalDateTime.of(2027, 10, 10, 18, 30), getCinemas()[0], 20, 120),
        Session(LocalDateTime.of(2025, 10, 10, 18, 30), getCinemas()[1], 10, 120),
    )
}
