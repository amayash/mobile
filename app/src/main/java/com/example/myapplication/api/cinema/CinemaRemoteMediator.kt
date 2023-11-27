package com.example.myapplication.api.cinema

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.session.toSession
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.repository.OfflineCinemaRepository
import com.example.myapplication.database.entities.repository.OfflineSessionRepository
import com.example.myapplication.database.remotekeys.model.RemoteKeyType
import com.example.myapplication.database.remotekeys.model.RemoteKeys
import com.example.myapplication.database.remotekeys.repository.OfflineRemoteKeyRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CinemaRemoteMediator(
    private val service: MyServerService,
    private val dbCinemaRepository: OfflineCinemaRepository,
    private val dbSessionRepository: OfflineSessionRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, Cinema>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Cinema>
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
            val cinemas = service.getCinemas(page, state.config.pageSize).map { it.toCinema() }
            val cinemasWithSessions = cinemas.map { cinema ->
                service.getSessionsForCinema(cinema.uid).map {
                    service.getSession(it.id).toSession()
                }
            }
            val endOfPaginationReached = cinemas.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.CINEMA)
                    dbSessionRepository.clearSessions()
                    dbCinemaRepository.clearCinemas()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = cinemas.map {
                    RemoteKeys(
                        entityId = it.uid,
                        type = RemoteKeyType.CINEMA,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbCinemaRepository.insertCinemas(cinemas)
                cinemasWithSessions.forEach {
                    try {
                        dbSessionRepository.insertSessions(it)
                    } catch (_: Exception) {
                    }
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Cinema>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { cinema ->
                dbRemoteKeyRepository.getAllRemoteKeys(cinema.uid, RemoteKeyType.CINEMA)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Cinema>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { cinema ->
                dbRemoteKeyRepository.getAllRemoteKeys(cinema.uid, RemoteKeyType.CINEMA)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Cinema>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uid?.let { cinemaUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(cinemaUid, RemoteKeyType.CINEMA)
            }
        }
    }

}