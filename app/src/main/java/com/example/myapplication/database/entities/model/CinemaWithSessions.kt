package com.example.myapplication.database.entities.model

data class CinemaWithSessions(
    val cinema: Cinema,
    val sessions: List<SessionFromCinema>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CinemaWithSessions

        if (cinema != other.cinema) return false
        if (sessions != other.sessions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cinema.hashCode()
        result = 31 * result + sessions.hashCode()
        return result
    }
}