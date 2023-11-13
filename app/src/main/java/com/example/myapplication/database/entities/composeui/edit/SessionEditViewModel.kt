package com.example.myapplication.database.entities.composeui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.repository.SessionRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class SessionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    var sessionUiState by mutableStateOf(SessionUiState())
        private set

    private val sessionUid: Int = checkNotNull(savedStateHandle["id"])
    private val cinemaUid: Int = checkNotNull(savedStateHandle["cinemaId"])

    init {
        viewModelScope.launch {
            if (sessionUid > 0) {
                sessionUiState = sessionRepository.getSession(sessionUid)
                    .filterNotNull()
                    .first()
                    .toUiState(true)
            }
        }
    }

    fun updateUiState(sessionDetails: SessionDetails) {
        sessionUiState = SessionUiState(
            sessionDetails = sessionDetails,
            isEntryValid = validateInput(sessionDetails)
        )
    }

    suspend fun saveSession() {
        if (validateInput()) {
            if (cinemaUid > 0)
                if (sessionUid > 0) {
                    sessionRepository.updateSession(sessionUiState.sessionDetails
                        .toSession(uid = sessionUid, cinemaUid = cinemaUid))
                } else {
                    sessionRepository.insertSession(sessionUiState.sessionDetails.toSession(cinemaUid = cinemaUid))
                }
        }
    }

    private fun validateInput(uiState: SessionDetails = sessionUiState.sessionDetails): Boolean {
        return with(uiState) {
            dateTime != LocalDateTime.MIN
                    && isValidDouble(price)
                    && maxCount > 0
                    && cinemaUid > 0
        }
    }
}

val regex = """^-?\d+(.\d+)?+(,\d+)?$""".toRegex()

fun isValidDouble(input: String): Boolean {
    return regex.matches(input)
}

data class SessionUiState(
    val sessionDetails: SessionDetails = SessionDetails(),
    val isEntryValid: Boolean = false
)

data class SessionDetails(
    val uid: Int = 0,
    val dateTime: LocalDateTime = LocalDateTime.MIN,
    val price: String = "0",
    val maxCount: Int = 0,
    val cinemaId: Int = 0
)

fun SessionDetails.toSession(uid: Int = 0, cinemaUid: Int = 0): Session = Session(
    uid = uid,
    dateTime = dateTime,
    price = price.toDoubleOrNull() ?: 0.0,
    maxCount = maxCount,
    cinemaId = cinemaUid
)

fun Session.toDetails(): SessionDetails = SessionDetails(
    uid = uid,
    dateTime = dateTime,
    price = price.toString(),
    maxCount = maxCount,
    cinemaId = cinemaId
)

fun Session.toUiState(isEntryValid: Boolean = false): SessionUiState = SessionUiState(
    sessionDetails = this.toDetails(),
    isEntryValid = isEntryValid
)