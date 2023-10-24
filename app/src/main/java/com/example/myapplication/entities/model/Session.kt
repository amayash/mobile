package com.example.myapplication.entities.model

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
    val uid: Int?,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    @ColumnInfo(name = "cinema_id", index = true)
    val cinemaId: Int?,
    @ColumnInfo(name = "max_count")
    val maxCount: Int
) {
    @Ignore
    constructor(
        dateTime: LocalDateTime,
        cinema: Cinema,
        maxCount: Int
    ) : this(null, dateTime, cinema.uid, maxCount)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Session
        if (uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        return uid ?: -1
    }
}

