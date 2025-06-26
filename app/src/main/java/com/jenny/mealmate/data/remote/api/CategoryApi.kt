package com.jenny.mealmate.data.remote.api

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.CategoryInfoDto
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoryApi {
    @GET("categories")
    suspend fun list(
        @Header("Authorization") auth: String,
    ): ApiResponseDto<List<CategoryInfoDto>>
}
