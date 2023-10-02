package com.example.myapplication.cinema.model

import com.example.myapplication.R
import java.io.Serializable

data class Cinema(
    val name: String,
    val description: String,
    val image: Int,
    val year: Long
) : Serializable

fun getCinemas(): List<Cinema> {
    return listOf(
        Cinema("First1", "new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema new cinema ", R.drawable.photo, 2023),
        Cinema("First2", "new cinema 123", R.drawable.photo, 2024),
    )
}
