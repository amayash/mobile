package com.example.myapplication.database.entities.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder

@Dao
interface OrderDao {
    @Query("select * from orders where user_id = :userId")
    fun getAll(userId: Int?): PagingSource<Int, Order>

    @Query(
        "SELECT o.*, s.*, os.count, os.frozen_price " +
                "FROM orders AS o " +
                "JOIN orders_sessions AS os ON os.order_id = o.uid " +
                "JOIN sessions AS s ON s.uid = os.session_id " +
                "WHERE o.uid = :orderId"
    )
    fun getByUid(orderId: Int?): List<SessionFromOrder>

    @Insert
    suspend fun insert(vararg order: Order): List<Long>

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()
}