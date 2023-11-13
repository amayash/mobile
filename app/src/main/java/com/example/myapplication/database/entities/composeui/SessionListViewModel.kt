package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModel
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCinema
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.SessionRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository

class SessionListViewModel(
    private val sessionRepository: SessionRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {
    suspend fun deleteSession(session: SessionFromCinema) {
        sessionRepository.deleteSession(
            Session(
                uid = session.uid,
                dateTime = session.dateTime,
                price = session.price,
                maxCount = 0,
                cinemaId = 0
            )
        )
    }

    suspend fun addSessionInCart(sessionId: Int, count: Int = 1) {
        try {
            userSessionRepository.insertUserSession(UserSessionCrossRef(1, sessionId, count))
        } catch (_: Exception) {

        }
    }
}