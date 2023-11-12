package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.repository.CinemaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CinemaListViewModel(private val cinemaRepository: CinemaRepository
) : ViewModel() {
    val cinemaListUiState: StateFlow<CinemaListUiState> = cinemaRepository.getAllCinemas().map {
        CinemaListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = CinemaListUiState()
    )

    suspend fun deleteCinema(cinema: Cinema) {
        cinemaRepository.deleteCinema(cinema)
    }
}

data class CinemaListUiState(val cinemaList: List<Cinema> = listOf())