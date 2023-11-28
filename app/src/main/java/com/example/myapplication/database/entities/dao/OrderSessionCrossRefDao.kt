package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.OrderSessionCrossRef

@Dao
interface OrderSessionCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orderSessionCrossRef: OrderSessionCrossRef)

    @Update
    suspend fun update(orderSessionCrossRef: OrderSessionCrossRef)

    @Delete
    suspend fun delete(orderSessionCrossRef: OrderSessionCrossRef)

    @Query("DELETE FROM orders_sessions where orders_sessions.order_id = :orderId")
    suspend fun deleteByOrderUid(orderId: Int)

    @Query("DELETE FROM orders_sessions where orders_sessions.session_id = :sessionId")
    suspend fun deleteBySessionUid(sessionId: Int)
}