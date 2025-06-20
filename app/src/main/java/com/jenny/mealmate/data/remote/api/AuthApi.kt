package com.jenny.mealmate.data.remote.api

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.AuthResponse
import com.jenny.mealmate.data.remote.dto.LoginDto
import com.jenny.mealmate.data.remote.dto.RegisterDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterDto) : ApiResponseDto<AuthResponse>

    @POST("login")
    suspend fun login(@Body body: LoginDto) : ApiResponseDto<AuthResponse>
}