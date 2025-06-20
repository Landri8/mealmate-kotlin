package com.jenny.mealmate.data.repository

import android.util.Log
import com.jenny.mealmate.data.remote.api.AuthApi
import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.AuthResponse
import com.jenny.mealmate.data.remote.dto.LoginDto
import com.jenny.mealmate.data.remote.dto.RegisterDto
import com.jenny.mealmate.data.remote.dto.UserDto
import com.jenny.mealmate.domain.repository.AuthRepository
import com.jenny.mealmate.util.ApiStatus
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
) : AuthRepository {
    override suspend fun register(username: String, email: String, password: String, confirmPassword: String): ApiResponseDto<AuthResponse> {
        return try {
            val response = api.register(RegisterDto(username, email, password, confirmPassword))
            response
        } catch (e: Exception) {
            ApiResponseDto(
                statusCode = ApiStatus.ERROR,
                message = "Something went wrong!",
                data = AuthResponse(
                    token = "",
                    user = UserDto(
                        id = "",
                        name = "",
                        email = "",
                        createdAt = ""
                    )
                )
            )
        }
    }

    override suspend fun login(email: String, password: String): ApiResponseDto<AuthResponse> {
        return try {
            val response = api.login(LoginDto(email, password))
            Log.d("NO", "SUCC ${response}")
            response
        } catch (e: Exception) {
            Log.d("ERR", "Fail")
            ApiResponseDto(
                statusCode = ApiStatus.ERROR,
                message = "Something went wrong!",
                data = AuthResponse(
                    token = "",
                    user = UserDto(
                        id = "",
                        name = "",
                        email = "",
                        createdAt = ""
                    )
                )
            )
        }
    }
}
