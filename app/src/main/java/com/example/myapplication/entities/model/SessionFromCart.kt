package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import androidx.room.Relation
import org.threeten.bp.LocalDateTime

data class SessionFromCart(
    @ColumnInfo(name = "uid")
    val uid: Int?,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    val price: Double,
    @ColumnInfo(name = "available_count")
    val availableCount: Int,
    val count: Int,
    @ColumnInfo(name = "cinema_id")
    val cinemaId: Int?,
    @Relation(
        parentColumn = "cinema_id",
        entity = Cinema::class,
        entityColumn = "uid"
    )    val cinema: Cinema
)