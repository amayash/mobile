package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Relation
import org.threeten.bp.LocalDateTime

data class SessionFromCart(
    @ColumnInfo(name = "uid")
    val uid: Int = 0,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    val price: Double,
    @ColumnInfo(name = "available_count")
    val availableCount: Int,
    val count: Int,
    @ColumnInfo(name = "cinema_id")
    val cinemaId: Int = 0,
    @Relation(
        parentColumn = "cinema_id",
        entity = Cinema::class,
        entityColumn = "uid"
    )    val cinema: Cinema
)