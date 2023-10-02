package com.example.myapplication.user.model

import java.io.Serializable

data class User(
    val login: String,
    val password: String
) : Serializable

fun getUsers(): List<User> {
    return listOf(
        User("login123", "password123"),
        User("login321", "password321"),
    )
}