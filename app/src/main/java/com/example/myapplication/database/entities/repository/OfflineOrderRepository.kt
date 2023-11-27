package com.example.myapplication.database.entities.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.myapplication.database.AppContainer
import com.example.myapplication.database.entities.dao.OrderDao
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import kotlinx.coroutines.flow.Flow

class OfflineOrderRepository(private val orderDao: OrderDao) : OrderRepository {
    override fun getAllOrders(userId: Int?): Flow<PagingData<Order>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { orderDao.getAll(userId) }
    ).flow

    override suspend fun getOrder(uid: Int): List<SessionFromOrder> = orderDao.getByUid(uid)

    override suspend fun insertOrder(order: Order): Long = orderDao.insert(order).first()

    fun getAllOrdersPagingSource(userId: Int?): PagingSource<Int, Order> = orderDao.getAll(userId)

    suspend fun clearOrders() = orderDao.deleteAll()

    suspend fun insertOrders(orders: List<Order>) = orderDao.insert(*orders.toTypedArray())
}