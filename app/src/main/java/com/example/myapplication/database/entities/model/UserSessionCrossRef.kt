package com.example.myapplication.database.entities.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Objects.hash

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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as UserSessionCrossRef
        if (userId == other.userId && sessionId == other.sessionId) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        return hash(userId, sessionId)
    }
}