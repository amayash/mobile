package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.dao.OrderDao
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.coroutines.flow.Flow

class OfflineOrderRepository(private val orderDao: OrderDao) : OrderRepository {
    override fun getAllOrders(userId: Int?): Flow<List<Order>> = orderDao.getAll(userId)

    override fun getOrder(orderId: Int?): List<SessionFromOrder> = orderDao.getByUid(orderId)

    override suspend fun insertOrder(order: Order) = orderDao.insert(order)

    override suspend fun updateOrder(order: Order) = orderDao.update(order)

    override suspend fun deleteOrder(order: Order) = orderDao.delete(order)
}