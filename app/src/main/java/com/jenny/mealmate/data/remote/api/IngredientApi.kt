package com.jenny.mealmate.data.remote.api

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.IngredientDto
import retrofit2.http.GET
import retrofit2.http.Header

interface IngredientApi {
    @GET("ingredients")
    suspend fun list(
        @Header("Authorization") auth: String,
    ): ApiResponseDto<List<IngredientDto>>
}
