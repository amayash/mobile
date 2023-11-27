package com.example.myapplication.database.entities.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.myapplication.database.AppContainer
import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.CinemaWithSessions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class OfflineCinemaRepository(private val cinemaDao: CinemaDao) : CinemaRepository {
    override fun getAllCinemas(): Flow<PagingData<Cinema>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = cinemaDao::getAll
    ).flow

    override suspend fun getCinema(uid: Int): CinemaWithSessions {
        val item = cinemaDao.getByUid(uid)
            .map { map ->
                map.firstNotNullOf {
                    CinemaWithSessions(
                        cinema = it.key,
                        sessions = it.value
                    )
                }
            }
            .first()
        return item
    }

    override suspend fun insertCinema(cinema: Cinema) = cinemaDao.insert(cinema)

    override suspend fun updateCinema(cinema: Cinema) = cinemaDao.update(cinema)

    override suspend fun deleteCinema(cinema: Cinema) = cinemaDao.delete(cinema)

    fun getAllCinemasPagingSource(): PagingSource<Int, Cinema> = cinemaDao.getAll()

    suspend fun insertCinemas(cinemas: List<Cinema>) =
        cinemaDao.insert(*cinemas.toTypedArray())

    suspend fun clearCinemas() = cinemaDao.deleteAll()
}