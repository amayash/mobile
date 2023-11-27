package com.example.myapplication.database.entities.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.CinemaWithSessions
import kotlinx.coroutines.flow.Flow

interface CinemaRepository {
    fun getAllCinemas(): Flow<PagingData<Cinema>>
    suspend fun getCinema(uid: Int): CinemaWithSessions
    suspend fun insertCinema(cinema: Cinema)
    suspend fun updateCinema(cinema: Cinema)
    suspend fun deleteCinema(cinema: Cinema)
}