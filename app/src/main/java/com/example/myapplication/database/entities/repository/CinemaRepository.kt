package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.SessionFromCinema
import kotlinx.coroutines.flow.Flow

interface CinemaRepository {
    fun getAllCinemas(): Flow<List<Cinema>>
    fun getCinemaWithSessions(uid: Int): Flow<Map<Cinema, List<SessionFromCinema>>?>
    fun getCinema(uid: Int): Flow<Map<Cinema, List<SessionFromCinema>>?>
    suspend fun insertCinema(cinema: Cinema)
    suspend fun updateCinema(cinema: Cinema)
    suspend fun deleteCinema(cinema: Cinema)
}