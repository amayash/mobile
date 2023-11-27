package com.example.myapplication.database.entities.composeui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.CinemaWithSessions
import com.example.myapplication.database.entities.repository.CinemaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CinemaViewModel(
    savedStateHandle: SavedStateHandle,
    private val cinemaRepository: CinemaRepository
) : ViewModel() {
    private val cinemaUid: Int = checkNotNull(savedStateHandle["id"])
    var cinemaUiState by mutableStateOf(CinemaUiState())
        private set

    init {
        viewModelScope.launch {
            if (cinemaUid > 0) {
                cinemaUiState = CinemaUiState(cinemaRepository.getCinema(cinemaUid))
            }
        }
    }
//    val cinemaUiState: mutableStateOf(CinemaUiState()) = cinemaRepository.getCinema(
//        cinemaUid
//    ).map {
//        CinemaUiState(it)
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
//        initialValue = CinemaUiState()
//    )
}

data class CinemaUiState(val cinemaWithSessions: CinemaWithSessions? = null)