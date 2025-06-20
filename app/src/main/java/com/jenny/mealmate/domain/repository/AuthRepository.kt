package com.jenny.mealmate.domain.repository

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.AuthResponse

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String, confirmPassword: String): ApiResponseDto<AuthResponse>
    suspend fun login(email: String, password: String): ApiResponseDto<AuthResponse>
}