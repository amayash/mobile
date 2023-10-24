package com.example.myapplication.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.model.OrderSessionCrossRef
import com.example.myapplication.entities.model.OrderWithSessions

@Dao
interface OrderSessionCrossRefDao {
    @Query(
        "SELECT * FROM orders_sessions " +
                "WHERE orders_sessions.order_id = :orderId "
    )
    fun getByUid(orderId: Int): OrderWithSessions

    @Query(
        "SELECT orders_sessions.count FROM orders_sessions " +
                "WHERE orders_sessions.order_id = :orderId AND " +
                "orders_sessions.session_id = :sessionId "
    )
    suspend fun getCountOfSessionsInOrder(orderId: Int, sessionId: Int): Int

    @Query(
        "SELECT SUM(orders_sessions.count) FROM orders_sessions " +
                "WHERE orders_sessions.session_id = :sessionId " +
                "group by :sessionId"
    )
    suspend fun getCountOfBusySessions(sessionId: Int): Int

    @Insert
    suspend fun insert(orderSessionCrossRef: OrderSessionCrossRef)

    @Update
    suspend fun update(orderSessionCrossRef: OrderSessionCrossRef)

    @Delete
    suspend fun delete(orderSessionCrossRef: OrderSessionCrossRef)
}