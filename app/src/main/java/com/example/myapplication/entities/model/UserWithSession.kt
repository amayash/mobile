package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import androidx.room.Relation

class UserWithSession(
    @ColumnInfo(name = "session_id")
    val sessionId: Int?,
    @Relation(
        parentColumn = "session_id",
        entity = Session::class,
        entityColumn = "uid"
    )
    val session: SessionWithCinema,
    @ColumnInfo(name = "count")
    val count: Int
)