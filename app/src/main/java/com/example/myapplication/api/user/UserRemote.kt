package com.example.myapplication.api.user

import com.example.myapplication.api.session.SessionFromCartRemote
import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    val id: Int = 0,
    val login: String = "",
    val password: String = "",
    var sessions: List<SessionFromCartRemote> = emptyList()
)