package com.jenny.mealmate.domain.usecases.auth

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.AuthResponse
import com.jenny.mealmate.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): ApiResponseDto<AuthResponse> {
        return repository.login(email, password)
    }
}