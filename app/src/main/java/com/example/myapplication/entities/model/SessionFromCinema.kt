package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDateTime

data class SessionFromCinema (
    @ColumnInfo(name = "uid")
    val uid: Int?,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    val price: Double,
    @ColumnInfo(name = "available_count")
    val availableCount: Int,
)