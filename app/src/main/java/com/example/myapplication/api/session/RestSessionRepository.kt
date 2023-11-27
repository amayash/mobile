package com.example.myapplication.api.session

import com.example.myapplication.api.MyServerService
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.toSession
import com.example.myapplication.database.entities.repository.OfflineSessionRepository
import com.example.myapplication.database.entities.repository.SessionRepository

class RestSessionRepository(
    private val service: MyServerService,
    private val dbSessionRepository: OfflineSessionRepository,
) : SessionRepository {
    override suspend fun getSession(uid: Int): Session {
        return service.getSession(uid).toSession()
    }

    override suspend fun insertSession(session: Session) {
        var session = service.createSession(session.toSessionRemote()).toSession()
        dbSessionRepository.insertSession(
            session
        )
    }

    override suspend fun updateSession(session: Session) {
        dbSessionRepository.updateSession(
            service.updateSession(
                session.uid,
                session.toSessionRemote()
            ).toSession()
        )
    }

    override suspend fun deleteSession(session: Session) {
        dbSessionRepository.deleteSession(
            service.deleteSession(session.uid).toSessionFromCinema().toSession()
        )
    }
}