package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrders(userId: Int?): Flow<List<Order>>
    fun getOrder(orderId: Int?): Flow<List<SessionFromOrder>>
    suspend fun insertOrder(order: Order): Long
    suspend fun updateOrder(order: Order)
    suspend fun deleteOrder(order: Order)
}