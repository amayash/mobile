package com.example.myapplication.database.entities.model

import org.threeten.bp.format.DateTimeFormatter

data class CinemaWithSessions(
    val cinema: Cinema,
    val sessions: List<SessionFromCinema>
)