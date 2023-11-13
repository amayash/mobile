package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCinema
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.CinemaRepository
import com.example.myapplication.database.entities.repository.SessionRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CinemaViewModel(
    savedStateHandle: SavedStateHandle,
    private val cinemaRepository: CinemaRepository,
    private val sessionRepository: SessionRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {
    private val cinemaUid: Int = checkNotNull(savedStateHandle["id"])

    var cinemaUiState: StateFlow<CinemaUiState> =
        cinemaRepository.getCinema(cinemaUid).map {
            CinemaUiState(it.cinema, it.sessions)
        }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = CinemaUiState()
    )

    suspend fun deleteSessionInCinema(session: Session) {
        sessionRepository.deleteSession(session)
    }

    suspend fun addSessionInCart(sessionId: Int, count: Int = 1) {
        try {
            userSessionRepository.insertUserSession(UserSessionCrossRef(1, sessionId, count))
        } catch (_: Exception) {

        }
    }
}

data class CinemaUiState(
    val cinema: Cinema? = null,
    val sessions: List<SessionFromCinema> = emptyList(),
)