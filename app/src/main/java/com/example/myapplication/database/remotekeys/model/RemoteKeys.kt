package com.example.myapplication.database.remotekeys.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.Session

enum class RemoteKeyType(private val type: String) {
    CINEMA(Cinema::class.simpleName ?: "Cinema"),
    ORDER(Order::class.simpleName ?: "Order"),
    SESSION(Session::class.simpleName ?: "Session");

    @TypeConverter
    fun toRemoteKeyType(value: String) = RemoteKeyType.values().first { it.type == value }

    @TypeConverter
    fun fromRemoteKeyType(value: RemoteKeyType) = value.type
}

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val entityId: Int,
    @TypeConverters(RemoteKeyType::class)
    val type: RemoteKeyType,
    val prevKey: Int?,
    val nextKey: Int?
)
