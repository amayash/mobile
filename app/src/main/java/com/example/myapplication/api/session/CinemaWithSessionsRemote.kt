package com.example.myapplication.api.session

import com.example.myapplication.api.cinema.CinemaRemote
import com.example.myapplication.api.cinema.toCinema
import com.example.myapplication.api.session.SessionFromCinemaRemote
import com.example.myapplication.api.session.toSessionFromCinema
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.CinemaWithSessions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
@Serializable
data class CinemaWithSessionsRemote(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: ByteArray? = null,
    val year: Long = 0,
    @SerialName("sessions")
    val sessions: List<SessionFromCinemaRemote>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CinemaWithSessionsRemote

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (year != other.year) return false
        if (sessions != other.sessions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + year.hashCode()
        result = 31 * result + sessions.hashCode()
        return result
    }
}

fun CinemaWithSessionsRemote.toCinemaWithSessions(): CinemaWithSessions = CinemaWithSessions(
    Cinema(
        id,
        name,
        description,
        image,
        year
    ),
    sessions.map { x -> x.toSessionFromCinema() }
)

fun Cinema.toCinemaWithSessionsRemote(): CinemaWithSessionsRemote = CinemaWithSessionsRemote(
    uid,
    name,
    description,
    image,
    year,
    sessions = emptyList()
)*/
