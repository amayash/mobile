package com.example.myapplication.entities.model

import androidx.room.Embedded
import androidx.room.Relation

data class CinemaWithSessions(
    @Embedded val cinema: Cinema,
    @Relation(
        parentColumn = "uid",
        entity = Session::class,
        entityColumn = "cinema_id"
    )
    val sessions: List<Session>
)