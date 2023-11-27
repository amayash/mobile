package com.example.myapplication.database.entities.repository

import androidx.paging.PagingData
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrders(userId: Int?): Flow<PagingData<Order>>
    suspend fun getOrder(uid: Int): List<SessionFromOrder>
    suspend fun insertOrder(order: Order): Long
}