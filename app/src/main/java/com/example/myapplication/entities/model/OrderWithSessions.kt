package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import androidx.room.Junction
import androidx.room.Relation

data class OrderWithSessions(
    @ColumnInfo(name = "order_id")
    val orderId: Int,

    @ColumnInfo(name = "session_id")
    val sessionId: Int,

    @Relation(
        parentColumn = "order_id",
        entity = Session::class,
        entityColumn = "uid",
        associateBy = Junction(
            value = OrderSessionCrossRef::class,
            parentColumn = "order_id",
            entityColumn = "session_id"
        )
    )
    val sessions: List<SessionWithCinema>
)
