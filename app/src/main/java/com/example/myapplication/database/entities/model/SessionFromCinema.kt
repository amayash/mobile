package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class SessionFromCinema (
    @ColumnInfo(name = "session_uid")
    val uid: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    val price: Double,
    @ColumnInfo(name = "available_count")
    val availableCount: Int,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        try {
            other as Session
            if (uid != other.uid) return false
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            if (dateFormatter.format(dateTime) != dateFormatter.format(other.dateTime)) return false
            if (price != other.price) return false
        } catch (_: Exception) {
            other as SessionFromCinema
            if (uid != other.uid) return false
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            if (dateFormatter.format(dateTime) != dateFormatter.format(other.dateTime)) return false
            if (price != other.price) return false
            if (availableCount != other.availableCount) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + availableCount.hashCode()
        return result
    }
}