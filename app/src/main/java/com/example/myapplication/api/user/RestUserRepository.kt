package com.example.myapplication.api.user

import android.util.Log
import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.cinema.toCinemaRemote
import com.example.myapplication.api.session.toSessionFromCart
import com.example.myapplication.api.session.toSessionFromCartRemote
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.User
import com.example.myapplication.database.entities.repository.OfflineCinemaRepository
import com.example.myapplication.database.entities.repository.OfflineUserRepository
import com.example.myapplication.database.entities.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class RestUserRepository(
    private val service: MyServerService,
    private val dbUserRepository: OfflineUserRepository,
    private val dbCinemaRepository: OfflineCinemaRepository,
) : UserRepository {
    override fun getAllUsers(): Flow<List<User>> {
        Log.d(RestUserRepository::class.simpleName, "Get users")
        return dbUserRepository.getAllUsers()
    }

    override suspend fun getCartByUser(userId: Int): List<SessionFromCart> {
        val cart = service.getUserCart(userId)
        return cart.sessions.map { x -> x.toSessionFromCart(dbCinemaRepository.getCinema(x.cinemaId).cinema.toCinemaRemote()) }
    }

    override suspend fun insertUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        /*val currentCart = service.getUserCart(user.uid)
        updateUserCart(user.uid,
            currentCart.sessions.map { x ->
                x.toSessionFromCart(dbCinemaRepository.getCinema(x.cinemaId).cinema.toCinemaRemote())
            })*/
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }
}