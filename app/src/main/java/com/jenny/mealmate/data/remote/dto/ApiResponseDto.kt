package com.jenny.mealmate.data.remote.dto

data class ApiResponseDto<T>(
    val statusCode: Int,
    val message: String,
    val data: T?
)