package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Relation
import org.threeten.bp.LocalDateTime

data class SessionFromOrder(
    @ColumnInfo(name = "uid")
    val uid: Int?,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    @ColumnInfo(name = "frozen_price")
    val frozenPrice: Double,
    val count: Int,
    @ColumnInfo(name = "cinema_id")
    val cinemaId: Int?,
    @Relation(
        parentColumn = "cinema_id",
        entity = Cinema::class,
        entityColumn = "uid"
    )
    val cinema: Cinema
)