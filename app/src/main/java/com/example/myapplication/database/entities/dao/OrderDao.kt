package com.example.myapplication.database.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("select * from orders where user_id = :userId")
    fun getAll(userId: Int?): Flow<List<Order>>

    @Query("SELECT o.*, s.*, os.count, os.frozen_price " +
            "FROM orders AS o " +
            "JOIN orders_sessions AS os ON os.order_id = o.uid " +
            "JOIN sessions AS s ON s.uid = os.session_id " +
            "WHERE o.uid = :orderId")
    fun getByUid(orderId: Int?): List<SessionFromOrder>

    @Insert
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)
}