package com.example.myapplication.database.entities.composeui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.model.OrderSessionCrossRef
import com.example.myapplication.database.entities.model.Session
import com.example.myapplication.database.entities.model.SessionFromCart
import com.example.myapplication.database.entities.model.UserSessionCrossRef
import com.example.myapplication.database.entities.repository.OrderRepository
import com.example.myapplication.database.entities.repository.OrderSessionRepository
import com.example.myapplication.database.entities.repository.UserRepository
import com.example.myapplication.database.entities.repository.UserSessionRepository
import kotlinx.coroutines.delay

class CartViewModel(
    private val userSessionRepository: UserSessionRepository,
    private val orderRepository: OrderRepository,
    private val orderSessionRepository: OrderSessionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val userUid: Int = 1
    var cartUiState by mutableStateOf(CartUiState())
        private set

    suspend fun refreshState() {
        val cart = userRepository.getCartByUser(userUid)
        cartUiState = CartUiState(cart)
    }

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
        refreshState()
    }

    suspend fun removeFromCart(user: Int, session: Session, count: Int = 1) {
        userSessionRepository.deleteUserSession(UserSessionCrossRef(user, session.uid, count))
        refreshState()
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
        refreshState()
        return true
    }
}

data class CartUiState(val sessionList: List<SessionFromCart> = listOf())