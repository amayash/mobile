package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Objects

@Entity(
    tableName = "orders_sessions",
    primaryKeys = ["order_id", "session_id"]
)
data class OrderSessionCrossRef(
    @ColumnInfo(name = "order_id", index = true)
    val orderId: Int,
    @ColumnInfo(name = "session_id", index = true)
    val sessionId: Int,
    @ColumnInfo(name = "frozen_price")
    val frozenPrice: Double,
    val count: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as OrderSessionCrossRef
        if (orderId == other.orderId && sessionId == other.sessionId) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(orderId, sessionId)
    }
}