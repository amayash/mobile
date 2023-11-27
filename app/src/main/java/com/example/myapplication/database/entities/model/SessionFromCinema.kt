package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class SessionFromCinema(
    @ColumnInfo(name = "session_uid")
    val uid: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    val price: Double,
    @ColumnInfo(name = "available_count")
    val availableCount: Int,
    @ColumnInfo(name = "cinema_id")
    val cinemaId: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SessionFromCinema
        if (uid != other.uid) return false
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        if (dateFormatter.format(dateTime) != dateFormatter.format(other.dateTime)) return false
        if (price != other.price) return false
        if (availableCount != other.availableCount) return false
        if (cinemaId != other.cinemaId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + availableCount.hashCode()
        result = 31 * result + cinemaId.hashCode()
        return result
    }
}

fun SessionFromCinema.toSession(): Session = Session (
    uid,
    dateTime,
    price,
    availableCount,
    cinemaId
)