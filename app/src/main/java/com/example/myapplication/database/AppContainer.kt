package com.example.myapplication.database

import android.content.Context
import com.example.myapplication.database.entities.repository.CinemaRepository
import com.example.myapplication.database.entities.repository.OfflineCinemaRepository
import com.example.myapplication.database.entities.repository.OfflineOrderRepository
import com.example.myapplication.database.entities.repository.OfflineOrderSessionRepository
import com.example.myapplication.database.entities.repository.OfflineSessionRepository
import com.example.myapplication.database.entities.repository.OfflineUserRepository
import com.example.myapplication.database.entities.repository.OfflineUserSessionRepository
import com.example.myapplication.database.entities.repository.OrderRepository
import com.example.myapplication.database.entities.repository.OrderSessionRepository
import com.example.myapplication.database.entities.repository.SessionRepository
import com.example.myapplication.database.entities.repository.UserRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository

interface AppContainer {
    val cinemaRepository: CinemaRepository
    val orderRepository: OrderRepository
    val orderSessionRepository: OrderSessionRepository
    val sessionRepository: SessionRepository
    val userRepository: UserRepository
    val userSessionRepository: UserSessionRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val cinemaRepository: CinemaRepository by lazy {
        OfflineCinemaRepository(AppDatabase.getInstance(context).cinemaDao())
    }
    override val orderRepository: OrderRepository by lazy {
        OfflineOrderRepository(AppDatabase.getInstance(context).orderDao())
    }
    override val orderSessionRepository: OrderSessionRepository by lazy {
        OfflineOrderSessionRepository(AppDatabase.getInstance(context).orderSessionCrossRefDao())
    }
    override val sessionRepository: SessionRepository by lazy {
        OfflineSessionRepository(AppDatabase.getInstance(context).sessionDao())
    }
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }
    override val userSessionRepository: UserSessionRepository by lazy {
        OfflineUserSessionRepository(AppDatabase.getInstance(context).userSessionCrossRefDao())
    }

    companion object {
        const val TIMEOUT = 5000L
    }
}