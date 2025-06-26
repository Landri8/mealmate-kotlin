package com.jenny.mealmate.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val createdAt: String
)