package com.example.myapplication.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("select * from orders where user_id = :userId")
    fun getAll(userId: Int?): Flow<List<Order>>

    @Insert
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)
}