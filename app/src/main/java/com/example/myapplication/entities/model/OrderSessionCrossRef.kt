package com.example.myapplication.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "orders_sessions",
    primaryKeys = ["order_id", "session_id"]
)
data class OrderSessionCrossRef(
    @ColumnInfo(name = "order_id", index = true)
    val orderId: Int,
    @ColumnInfo(name = "session_id", index = true)
    val sessionId: Int,
    @ColumnInfo(name = "count")
    val count: Int,
)