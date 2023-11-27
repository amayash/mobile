package com.example.myapplication.database.entities.composeui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.repository.CinemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class CinemaListViewModel(
    private val cinemaRepository: CinemaRepository
) : ViewModel() {
    val cinemaListUiState: Flow<PagingData<Cinema>> = cinemaRepository.getAllCinemas()

    suspend fun deleteCinema(cinema: Cinema) {
        cinemaRepository.deleteCinema(cinema)
    }
}