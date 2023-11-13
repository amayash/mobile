package com.example.myapplication.database.entities.repository

import androidx.paging.PagingSource
import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.CinemaWithSessions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OfflineCinemaRepository(private val cinemaDao: CinemaDao) : CinemaRepository {
    override fun getAllCinemas(): Flow<List<Cinema>> = cinemaDao.getAll()

    override fun getAllCinemasPaged(): PagingSource<Int, Cinema> = cinemaDao.getAllCinemasPaged()

    override fun getCinema(uid: Int): Flow<CinemaWithSessions> {
        return flow {
            cinemaDao.getByUid(uid).collect {
                emit(it.firstNotNullOf {
                    CinemaWithSessions(
                        cinema = it.key,
                        sessions = it.value
                    )
                })
            }
        }
    }

    override suspend fun insertCinema(cinema: Cinema) = cinemaDao.insert(cinema)

    override suspend fun updateCinema(cinema: Cinema) = cinemaDao.update(cinema)

    override suspend fun deleteCinema(cinema: Cinema) = cinemaDao.delete(cinema)
}