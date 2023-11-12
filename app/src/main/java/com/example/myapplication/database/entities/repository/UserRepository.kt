package com.example.myapplication.database.entities.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    fun getCartByUser(userId: Int): Flow<List<SessionFromCart>>
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}