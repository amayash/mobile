package com.example.myapplication.api.cinema

import com.example.myapplication.database.entities.model.Cinema
import kotlinx.serialization.Serializable

@Serializable
data class CinemaRemote(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: ByteArray? = null,
    val year: Long = 0
)

fun CinemaRemote.toCinema(): Cinema = Cinema(
    id,
    name,
    description,
    image,
    year
)

fun Cinema.toCinemaRemote(): CinemaRemote = CinemaRemote(
    uid,
    name,
    description,
    image,
    year
)