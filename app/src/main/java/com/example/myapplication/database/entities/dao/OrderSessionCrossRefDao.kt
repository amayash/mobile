package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.myapplication.database.entities.model.OrderSessionCrossRef

@Dao
interface OrderSessionCrossRefDao {
    @Insert
    suspend fun insert(orderSessionCrossRef: OrderSessionCrossRef)

    @Update
    suspend fun update(orderSessionCrossRef: OrderSessionCrossRef)

    @Delete
    suspend fun delete(orderSessionCrossRef: OrderSessionCrossRef)
}