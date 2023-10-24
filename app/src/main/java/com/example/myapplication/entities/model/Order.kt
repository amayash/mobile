package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "user_id", index = true)
    val userId: Int?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Order
        if (uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        return uid
    }
}

