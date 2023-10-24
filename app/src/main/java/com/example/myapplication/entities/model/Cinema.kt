package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "cinemas")
data class Cinema(
    @PrimaryKey(autoGenerate = true)
    val uid: Int?,
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
    ) : this(null, name, description, image, year)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Cinema
        if (uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        return uid ?: -1
    }
}

