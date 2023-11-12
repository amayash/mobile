package com.example.myapplication.database.entities.composeui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.repository.CinemaRepository
import kotlinx.coroutines.launch

class CinemaDropDownViewModel(
    private val cinemaRepository: CinemaRepository
) : ViewModel() {
    var cinemasListUiState by mutableStateOf(CinemasListUiState())
        private set

    var cinemaUiState by mutableStateOf(LocalCinemaUiState())
        private set

    init {
        viewModelScope.launch {
            val cinemaList = mutableListOf<Cinema>()
            cinemaRepository.getAllCinemas().collect { cinemas ->
                cinemaList.clear()
                cinemaList.addAll(cinemas)
                cinemasListUiState = CinemasListUiState(cinemaList)
            }
        }
    }

    fun setCurrentCinema(cinemaId: Int) {
        val cinema: Cinema? =
            cinemasListUiState.cinemaList.firstOrNull { cinema -> cinema.uid == cinemaId }
        cinema?.let { updateUiState(it) }
    }

    fun updateUiState(cinema: Cinema) {
        cinemaUiState = LocalCinemaUiState(
            cinema = cinema
        )
    }
}

data class CinemasListUiState(val cinemaList: List<Cinema> = listOf())

data class LocalCinemaUiState(
    val cinema: Cinema? = null
)

fun Cinema.toUiState() = LocalCinemaUiState(cinema = Cinema(
    uid = uid,
    name = name,
    description = description,
    image = image,
    year = year
    )
)