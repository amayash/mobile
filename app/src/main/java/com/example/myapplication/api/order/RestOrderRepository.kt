package com.example.myapplication.api.order

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.api.MyServerService
import com.example.myapplication.database.AppContainer
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.SessionFromOrder
import com.example.myapplication.database.entities.repository.OfflineOrderRepository
import com.example.myapplication.database.entities.repository.OrderRepository
import com.example.myapplication.database.remotekeys.repository.OfflineRemoteKeyRepository
import kotlinx.coroutines.flow.Flow

class RestOrderRepository(
    private val service: MyServerService,
    private val dbOrderRepository: OfflineOrderRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : OrderRepository {
    override fun getAllOrders(userId: Int?): Flow<PagingData<Order>> {
        Log.d(RestOrderRepository::class.simpleName, "Get orders")

        val pagingSourceFactory = { dbOrderRepository.getAllOrdersPagingSource(userId) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = OrderRemoteMediator(
                service,
                dbOrderRepository,
                dbRemoteKeyRepository,
                database,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getOrder(uid: Int): List<SessionFromOrder> =
        service.getOrder(uid).toListSessionsFromOrder()

    override suspend fun insertOrder(order: Order): Long {
        return service.createOrder(order.toOrderRemote()).toOrder().uid.toLong()
    }
}