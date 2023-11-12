package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "cinemas")
data class Cinema(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val name: String,
    val description: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray?,
    val year: Long
) {
    @Ignore
    constructor(
        name: String,
        description: String,
        image: ByteArray?,
        year: Long
    ) : this(0, name, description, image, year)

    companion object {
        fun getCinema(index: Int = 0): Cinema {
            return Cinema(
                index,
                "name",
                "desc",
                byteArrayOf(),
                0,
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Cinema
        if (uid != other.uid) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (!image.contentEquals(other.image)) return false
        if (year != other.year) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + year.hashCode()
        return result
    }
}

