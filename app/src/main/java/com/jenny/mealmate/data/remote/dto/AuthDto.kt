package com.jenny.mealmate.data.remote.dto

data class  UserDto (
    val id: String,
    val name: String,
    val email: String,
    val createdAt: String
)

data class RegisterDto(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

data class LoginDto(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)