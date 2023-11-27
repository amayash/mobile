package com.example.myapplication.api.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.api.MyServerService
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.model.User
import com.example.myapplication.database.entities.repository.OfflineUserRepository
import com.example.myapplication.database.remotekeys.model.RemoteKeyType
import com.example.myapplication.database.remotekeys.model.RemoteKeys
import com.example.myapplication.database.remotekeys.repository.OfflineRemoteKeyRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val service: MyServerService,
    private val dbUserRepository: OfflineUserRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        try {
            val users = service.getUsers().map { it.toUser() }
            val endOfPaginationReached = users.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.CINEMA)
                    dbUserRepository.clearUsers()
                }
                val keys = users.map {
                    RemoteKeys(
                        entityId = it.uid,
                        type = RemoteKeyType.CINEMA,
                        prevKey = null,
                        nextKey = null
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbUserRepository.insertUsers(users)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

}