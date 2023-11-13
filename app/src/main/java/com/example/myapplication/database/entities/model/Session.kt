package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "sessions", foreignKeys = [
        ForeignKey(
            entity = Cinema::class,
            parentColumns = ["uid"],
            childColumns = ["cinema_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class Session(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    val price: Double,
    @ColumnInfo(name = "max_count")
    val maxCount: Int,
    @ColumnInfo(name = "cinema_id", index = true)
    val cinemaId: Int = 0,
) {
    @Ignore
    constructor(
        dateTime: LocalDateTime,
        price: Double,
        maxCount: Int,
        cinema: Cinema,
    ) : this(0, dateTime, price, maxCount, cinema.uid)

    companion object {
        fun getSession(index: Int = 0): Session {
            return Session(
                index,
                LocalDateTime.MIN,
                0.0,
                0,
                0
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Session
        if (uid != other.uid) return false
        if (dateTime != other.dateTime) return false
        if (price != other.price) return false
        if (maxCount != other.maxCount) return false
        if (cinemaId != other.cinemaId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + maxCount.hashCode()
        result = 31 * result + cinemaId.hashCode()
        return result
    }
}

