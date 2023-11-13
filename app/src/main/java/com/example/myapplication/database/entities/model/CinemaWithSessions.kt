package com.example.myapplication.database.entities.model

data class CinemaWithSessions(
    val cinema: Cinema,
    val sessions: List<SessionFromCinema>
)