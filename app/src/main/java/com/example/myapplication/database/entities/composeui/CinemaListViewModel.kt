package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.repository.CinemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CinemaListViewModel(private val cinemaRepository: CinemaRepository
) : ViewModel() {
    suspend fun deleteCinema(cinema: Cinema) {
        cinemaRepository.deleteCinema(cinema)
    }

    fun call(): Flow<PagingData<Cinema>> = Pager(
        PagingConfig(
            pageSize = 4,
            prefetchDistance = 4,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            cinemaRepository.getAllCinemasPaged()
        }
    ).flow.cachedIn(viewModelScope)
}