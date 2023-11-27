package com.example.myapplication.database

import android.content.Context
import com.example.myapplication.api.MyServerService
import com.example.myapplication.api.cinema.RestCinemaRepository
import com.example.myapplication.api.order.RestOrderRepository
import com.example.myapplication.api.ordersession.RestOrderSessionRepository
import com.example.myapplication.api.session.RestSessionRepository
import com.example.myapplication.api.user.RestUserRepository
import com.example.myapplication.api.usersession.RestUserSessionRepository
import com.example.myapplication.database.entities.repository.OfflineCinemaRepository
import com.example.myapplication.database.entities.repository.OfflineOrderRepository
import com.example.myapplication.database.entities.repository.OfflineOrderSessionRepository
import com.example.myapplication.database.entities.repository.OfflineSessionRepository
import com.example.myapplication.database.entities.repository.OfflineUserRepository
import com.example.myapplication.database.entities.repository.OfflineUserSessionRepository
import com.example.myapplication.database.remotekeys.repository.OfflineRemoteKeyRepository

interface AppContainer {
    val cinemaRestRepository: RestCinemaRepository
    val sessionRestRepository: RestSessionRepository
    val userRestRepository: RestUserRepository
    val orderRestRepository: RestOrderRepository
    val orderSessionRestRepository: RestOrderSessionRepository
    val userSessionRestRepository: RestUserSessionRepository

    companion object {
        const val TIMEOUT = 5000L
        const val LIMIT = 10
    }
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val cinemaRepository: OfflineCinemaRepository by lazy {
        OfflineCinemaRepository(AppDatabase.getInstance(context).cinemaDao())
    }
    private val orderRepository: OfflineOrderRepository by lazy {
        OfflineOrderRepository(AppDatabase.getInstance(context).orderDao())
    }
    private val orderSessionRepository: OfflineOrderSessionRepository by lazy {
        OfflineOrderSessionRepository(AppDatabase.getInstance(context).orderSessionCrossRefDao())
    }
    private val sessionRepository: OfflineSessionRepository by lazy {
        OfflineSessionRepository(AppDatabase.getInstance(context).sessionDao())
    }
    private val userRepository: OfflineUserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }
    private val userSessionRepository: OfflineUserSessionRepository by lazy {
        OfflineUserSessionRepository(AppDatabase.getInstance(context).userSessionCrossRefDao())
    }
    private val remoteKeyRepository: OfflineRemoteKeyRepository by lazy {
        OfflineRemoteKeyRepository(AppDatabase.getInstance(context).remoteKeysDao())
    }
    override val cinemaRestRepository: RestCinemaRepository by lazy {
        RestCinemaRepository(
            MyServerService.getInstance(),
            cinemaRepository,
            sessionRepository,
            remoteKeyRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val sessionRestRepository: RestSessionRepository by lazy {
        RestSessionRepository(
            MyServerService.getInstance(),
            sessionRepository,
            userSessionRepository,
            orderSessionRepository,
        )
    }
    override val userRestRepository: RestUserRepository by lazy {
        RestUserRepository(
            MyServerService.getInstance(),
            userRepository,
            userSessionRepository,
        )
    }
    override val orderRestRepository: RestOrderRepository by lazy {
        RestOrderRepository(
            MyServerService.getInstance(),
            orderRepository,
            cinemaRepository,
            orderSessionRepository,
            remoteKeyRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val userSessionRestRepository: RestUserSessionRepository by lazy {
        RestUserSessionRepository(
            MyServerService.getInstance(),
            userSessionRepository,
        )
    }
    override val orderSessionRestRepository: RestOrderSessionRepository by lazy {
        RestOrderSessionRepository(
            MyServerService.getInstance(),
            orderSessionRepository,
        )
    }
}