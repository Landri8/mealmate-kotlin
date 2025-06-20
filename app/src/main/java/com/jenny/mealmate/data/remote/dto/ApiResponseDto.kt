package com.jenny.mealmate.data.remote.dto

import com.jenny.mealmate.util.ApiStatus

data class ApiResponseDto<T>(
    val statusCode: ApiStatus,
    val message: String,
    val data: T
)