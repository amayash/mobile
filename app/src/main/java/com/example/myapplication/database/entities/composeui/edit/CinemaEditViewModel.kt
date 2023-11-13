package com.example.myapplication.database.entities.composeui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.CinemaWithSessions
import com.example.myapplication.database.entities.model.SessionFromCinema
import com.example.myapplication.database.entities.repository.CinemaRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CinemaEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val cinemaRepository: CinemaRepository
) : ViewModel() {
    var cinemaUiState by mutableStateOf(CinemaUiState())
        private set

    private val cinemaUid: Int = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            if (cinemaUid > 0) {
                cinemaUiState = cinemaRepository.getCinema(cinemaUid)
                    .filterNotNull()
                    .first()
                    .toUiState(true)
            }
        }
    }

    fun updateUiState(cinemaDetails: CinemaDetails) {
        cinemaUiState = CinemaUiState(
            cinemaDetails = cinemaDetails,
            isEntryValid = validateInput(cinemaDetails)
        )
    }

    suspend fun saveCinema() {
        if (validateInput()) {
            if (cinemaUid > 0) {
                cinemaRepository.updateCinema(cinemaUiState.cinemaDetails.toCinema(cinemaUid))
            } else {
                cinemaRepository.insertCinema(cinemaUiState.cinemaDetails.toCinema())
            }
        }
    }

    private fun validateInput(uiState: CinemaDetails = cinemaUiState.cinemaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
                    && description.isNotBlank()
                    && year >= 1900
                    && year <= 2100
        }
    }
}

data class CinemaUiState(
    val cinemaDetails: CinemaDetails = CinemaDetails(),
    val isEntryValid: Boolean = false
)

data class CinemaDetails(
    val name: String = "",
    val description: String = "",
    val image: ByteArray? = byteArrayOf(),
    val year: Long = 1900,
    val sessions: List<SessionFromCinema> = emptyList()
)

fun CinemaDetails.toCinema(uid: Int = 0): Cinema = Cinema(
    uid = uid,
    name = name,
    description = description,
    image = image,
    year = year
)

fun CinemaWithSessions.toDetails(): CinemaDetails {
    val cinema = this.cinema
    val sessions = this.sessions

    return CinemaDetails(
        name = cinema.name,
        description = cinema.description,
        image = cinema.image,
        year = cinema.year,
        sessions = sessions
    )
}

fun CinemaWithSessions.toUiState(isEntryValid: Boolean = false): CinemaUiState = CinemaUiState(
    cinemaDetails = this.toDetails(),
    isEntryValid = isEntryValid
)