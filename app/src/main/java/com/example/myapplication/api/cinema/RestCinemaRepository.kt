package com.example.myapplication.api.cinema

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.api.MyServerService
import com.example.myapplication.database.AppContainer
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.CinemaWithSessions
import com.example.myapplication.database.entities.model.SessionFromCinema
import com.example.myapplication.database.entities.model.toSession
import com.example.myapplication.database.entities.repository.CinemaRepository
import com.example.myapplication.database.entities.repository.OfflineCinemaRepository
import com.example.myapplication.database.entities.repository.OfflineSessionRepository
import com.example.myapplication.database.remotekeys.repository.OfflineRemoteKeyRepository
import kotlinx.coroutines.flow.Flow

class RestCinemaRepository(
    private val service: MyServerService,
    private val dbCinemaRepository: OfflineCinemaRepository,
    private val dbSessionRepository: OfflineSessionRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : CinemaRepository {
    override fun getAllCinemas(): Flow<PagingData<Cinema>> {
        Log.d(RestCinemaRepository::class.simpleName, "Get cinemas")

        val pagingSourceFactory = { dbCinemaRepository.getAllCinemasPagingSource() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = CinemaRemoteMediator(
                service,
                dbCinemaRepository,
                dbSessionRepository,
                dbRemoteKeyRepository,
                database,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getCinema(uid: Int): CinemaWithSessions {
        val cinema = service.getCinema(uid).toCinema()
        val sessions = service.getSessionsForCinema(uid).map { x ->
            SessionFromCinema(
                x.id,
                x.dateTime,
                x.price,
                x.availableCount,
                uid
            )
        }
        return CinemaWithSessions(cinema, sessions)
    }

    override suspend fun insertCinema(cinema: Cinema) {
        service.createCinema(cinema.toCinemaRemote()).toCinema()
    }

    override suspend fun updateCinema(cinema: Cinema) {
        service.updateCinema(cinema.uid, cinema.toCinemaRemote()).toCinema()
    }

    override suspend fun deleteCinema(cinema: Cinema) {
        service.deleteCinema(cinema.uid).toCinema()
    }
}