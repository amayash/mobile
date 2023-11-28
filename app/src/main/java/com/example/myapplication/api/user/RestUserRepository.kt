package com.example.myapplication.api.user

import android.util.Log
import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.session.toSessionFromCart
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.User
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.OfflineUserRepository
import com.example.myapplication.database.entities.repository.OfflineUserSessionRepository
import com.example.myapplication.database.entities.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class RestUserRepository(
    private val service: MyServerService,
    private val dbUserRepository: OfflineUserRepository,
    private val dbUserSessionRepository: OfflineUserSessionRepository,
) : UserRepository {
    override fun getAllUsers(): Flow<List<User>> {
        Log.d(RestUserRepository::class.simpleName, "Get users")
        return dbUserRepository.getAllUsers()
    }

    override suspend fun getCartByUser(userId: Int): List<SessionFromCart> {
        val cart = service.getUserCart(userId)
        dbUserSessionRepository.deleteUserSessions(userId)
        cart.sessions.map { sessionFromCartRemote ->
            dbUserSessionRepository.insertUserSession(
                UserSessionCrossRef(
                    userId,
                    sessionFromCartRemote.id,
                    sessionFromCartRemote.count
                )
            )
        }

        return cart.sessions.map {
            val session = service.getSession(it.id)
            it.toSessionFromCart(
                session.cinema,
                session.dateTime,
                session.price,
                session.maxCount - service.getOrders().flatMap { order ->
                    order.sessions.filter { session -> session.id == it.id }
                }.sumOf { session -> session.count })
        }
    }

    override suspend fun insertUser(user: User) {
    }

    override suspend fun updateUser(user: User) {
    }

    override suspend fun deleteUser(user: User) {
    }
}