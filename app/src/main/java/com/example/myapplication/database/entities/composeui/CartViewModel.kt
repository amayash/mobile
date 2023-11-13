package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.OrderSessionCrossRef
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.OrderRepository
import com.example.myapplication.database.entities.repository.OrderSessionRepository
import com.example.myapplication.database.entities.repository.UserRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel(
    private val userSessionRepository: UserSessionRepository,
    private val orderRepository: OrderRepository,
    private val orderSessionRepository: OrderSessionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val userUid: Int = 1

    val cartUiState: StateFlow<CartUiState> = userRepository.getCartByUser(userUid).map {
        CartUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = CartUiState()
    )

    suspend fun addToOrder(userId: Int, sessions: List<SessionFromCart>) {
        if (sessions.isEmpty())
            return
        val orderId = orderRepository.insertOrder(Order(0, userId))
        sessions.forEach { session ->
            orderSessionRepository.insertOrderSession(
                OrderSessionCrossRef(
                    orderId.toInt(),
                    session.uid,
                    session.price,
                    session.count
                )
            )
        }
        userSessionRepository.deleteUserSessions(userId)
    }

    suspend fun removeFromCart(user: Int, session: Session, count: Int = 1) {
        userSessionRepository.deleteUserSession(UserSessionCrossRef(user, session.uid, count))
    }

    suspend fun updateFromCart(userId: Int, session: Session, count: Int, availableCount: Int)
            : Boolean {
        if (count == 0) {
            removeFromCart(userId, session, count)
            return false
        }
        if (count > availableCount)
            return false
        userSessionRepository.updateUserSession(UserSessionCrossRef(userId, session.uid, count))
        return true
    }
}

data class CartUiState(val sessionList: List<SessionFromCart> = listOf())