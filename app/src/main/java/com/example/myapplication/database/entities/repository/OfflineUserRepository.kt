package com.example.myapplication.database.entities.repository

import com.example.myapplication.database.entities.dao.UserDao
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<User>> = userDao.getAll()

    override fun getCartByUser(userId: Int): Flow<List<SessionFromCart>> =
        userDao.getCartByUid(userId)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)
}