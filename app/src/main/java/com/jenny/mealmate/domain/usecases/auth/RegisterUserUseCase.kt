package com.jenny.mealmate.domain.usecases.auth

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.AuthResponse
import com.jenny.mealmate.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String, confirmPassword : String): ApiResponseDto<AuthResponse> {
        return repository.register(username, email, password, confirmPassword)
    }
}
