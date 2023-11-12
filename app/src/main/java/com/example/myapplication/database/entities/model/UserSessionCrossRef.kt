package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "users_sessions",
    primaryKeys = ["user_id", "session_id"]
)
data class UserSessionCrossRef(
    @ColumnInfo(name = "user_id", index = true)
    val userId: Int,
    @ColumnInfo(name = "session_id", index = true)
    val sessionId: Int,
    @ColumnInfo(name = "count")
    val count: Int,
)