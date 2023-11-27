package com.example.myapplication.database.entities.composeui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myapplication.database.AppDataContainer
import com.example.myapplication.database.entities.model.Order
import com.example.myapplication.database.entities.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderListViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    val orderListUiState: Flow<PagingData<Order>> = orderRepository.getAllOrders(1)
}

data class OrderListUiState(val orderList: List<Order> = listOf())