package com.example.myapplication.api.order

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.api.MyServerService
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.repository.OfflineOrderRepository
import com.example.myapplication.database.remotekeys.model.RemoteKeyType
import com.example.myapplication.database.remotekeys.model.RemoteKeys
import com.example.myapplication.database.remotekeys.repository.OfflineRemoteKeyRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class OrderRemoteMediator(
    private val service: MyServerService,
    private val dbOrderRepository: OfflineOrderRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, Order>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Order>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val orders = service.getOrders(page, state.config.pageSize).map { it.toOrder() }
            val endOfPaginationReached = orders.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.ORDER)
                    dbOrderRepository.clearOrders()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = orders.map {
                    RemoteKeys(
                        entityId = it.uid,
                        type = RemoteKeyType.ORDER,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbOrderRepository.insertOrders(orders)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Order>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { order ->
                dbRemoteKeyRepository.getAllRemoteKeys(order.uid, RemoteKeyType.ORDER)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Order>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { order ->
                dbRemoteKeyRepository.getAllRemoteKeys(order.uid, RemoteKeyType.ORDER)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Order>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uid?.let { orderUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(orderUid, RemoteKeyType.ORDER)
            }
        }
    }

}