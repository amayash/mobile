package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.dao.OrderSessionCrossRefDao
import com.example.myapplication.database.entities.model.OrderSessionCrossRef

class OfflineOrderSessionRepository(private val orderSessionDao: OrderSessionCrossRefDao) : OrderSessionRepository {
    override suspend fun insertOrderSession(orderSessionCrossRef: OrderSessionCrossRef) = orderSessionDao.insert(orderSessionCrossRef)

    override suspend fun updateOrderSession(orderSessionCrossRef: OrderSessionCrossRef) = orderSessionDao.update(orderSessionCrossRef)

    override suspend fun deleteOrderSession(orderSessionCrossRef: OrderSessionCrossRef) = orderSessionDao.delete(orderSessionCrossRef)
}