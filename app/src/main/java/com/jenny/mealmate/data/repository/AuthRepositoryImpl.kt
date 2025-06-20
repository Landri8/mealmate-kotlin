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
    override suspend fun register(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResponseDto<AuthResponse> {
        return try {
            Log.d("API_REGISTER", "Calling register() with: $username, $email")

            val request = RegisterDto(username, email, password, confirmPassword)
            val response = api.register(request)

            Log.d("API_REGISTER", "Received response: status=${response}")

            response
        } catch (e: Exception) {
            Log.e("API_REGISTER", "Exception: ${e.localizedMessage}", e)

            ApiResponseDto(
                statusCode = ApiStatus.ERROR.code,
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
            Log.d("API_LOGIN", "Calling login() with: $email")

            val request = LoginDto(email, password)
            val response = api.login(request)

            Log.d("API_LOGIN", "Received response: ${response}")

            response
        } catch (e: Exception) {
            Log.e("API_LOGIN", "Exception: ${e.localizedMessage}", e)

            ApiResponseDto(
                statusCode = ApiStatus.ERROR.code,
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
