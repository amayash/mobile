package com.example.myapplication.database.entities.composeui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.entities.model.CinemaWithSessions
import com.example.myapplication.database.entities.repository.CinemaRepository

class CinemaViewModel(
    savedStateHandle: SavedStateHandle, private val cinemaRepository: CinemaRepository
) : ViewModel() {
    private val cinemaUid: Int = checkNotNull(savedStateHandle["id"])

    var cinemaUiState by mutableStateOf(CinemaUiState())
        private set

    suspend fun refreshState() {
        if (cinemaUid > 0) {
            cinemaUiState = CinemaUiState(cinemaRepository.getCinema(cinemaUid))
        }
    }

//    init {
//        viewModelScope.launch {
//            if (cinemaUid > 0) {
//                cinemaUiState = CinemaUiState(cinemaRepository.getCinema(cinemaUid))
//            }
//        }
//    }

//    val cinemaUiState: mutableStateOf(CinemaUiState()) = cinemaRepository.getCinema(
//    cinemaUid
//    ).map
//    {
//        CinemaUiState(it)
//    }.stateIn(
//    scope = viewModelScope,
//    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
//    initialValue = CinemaUiState()
//    )
}

data class CinemaUiState(val cinemaWithSessions: CinemaWithSessions? = null)