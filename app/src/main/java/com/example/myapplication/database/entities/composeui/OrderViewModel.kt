package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppContainer
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.SessionFromOrder
import com.example.myapplication.database.entities.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val orderUid: Int = checkNotNull(savedStateHandle["id"])

    val orderUiState: StateFlow<OrderUiState> = flow{emit(orderRepository.getOrder(orderUid))} .map {
        OrderUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppContainer.TIMEOUT),
        initialValue = OrderUiState()
    )
}

data class OrderUiState(val sessionList: List<SessionFromOrder> = listOf())