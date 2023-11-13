package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OrderListViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    val orderListUiState: StateFlow<OrderListUiState> = orderRepository.getAllOrders(1).map {
        OrderListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = OrderListUiState()
    )
}

data class OrderListUiState(val orderList: List<Order> = listOf())