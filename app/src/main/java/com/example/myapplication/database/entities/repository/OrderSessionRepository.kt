package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.model.OrderSessionCrossRef

interface OrderSessionRepository {
    suspend fun insertOrderSession(orderSessionCrossRef: OrderSessionCrossRef)
    suspend fun updateOrderSession(orderSessionCrossRef: OrderSessionCrossRef)
    suspend fun deleteOrderSession(orderSessionCrossRef: OrderSessionCrossRef)
}