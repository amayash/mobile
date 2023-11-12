package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.dao.CinemaDao
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.SessionFromCinema
import kotlinx.coroutines.flow.Flow

class OfflineCinemaRepository(private val cinemaDao: CinemaDao) : CinemaRepository {
    override fun getAllCinemas(): Flow<List<Cinema>> = cinemaDao.getAll()
    override fun getCinemaWithSessions(uid: Int): Flow<Map<Cinema, List<SessionFromCinema>>?> {
        TODO("Not yet implemented")
    }

    override fun getCinema(uid: Int):  Flow<Map<Cinema, List<SessionFromCinema>>?> = cinemaDao.getByUid(uid)

    override suspend fun insertCinema(cinema: Cinema) = cinemaDao.insert(cinema)

    override suspend fun updateCinema(cinema: Cinema) = cinemaDao.update(cinema)

    override suspend fun deleteCinema(cinema: Cinema) = cinemaDao.delete(cinema)
}