package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getCartByUser(userId: Int): List<SessionFromCart>
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}