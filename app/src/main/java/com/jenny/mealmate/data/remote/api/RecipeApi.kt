package com.jenny.mealmate.data.remote.api

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.RecipeDetailDto
import com.jenny.mealmate.data.remote.dto.SaveRecipeRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApi {
    @GET("recipes/{id}")
    suspend fun getRecipeDetail(
        @Header("Authorization") bearer: String,
        @Path("id")        recipeId: String
    ): ApiResponseDto<RecipeDetailDto>

    @POST("recipes/create")
    suspend fun saveRecipe(
        @Header("Authorization") bearer: String,
        @Body               req: SaveRecipeRequestDto
    ): ApiResponseDto<RecipeDetailDto>

    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(
        @Header("Authorization") auth: String,
        @Path("id") recipeId: String
    ): ApiResponseDto<Unit>
}
